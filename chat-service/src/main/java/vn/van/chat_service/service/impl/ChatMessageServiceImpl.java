package vn.van.chat_service.service.impl;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.van.chat_service.constant.ResponseMessage;
import vn.van.chat_service.dto.request.ChatMessageCreateRequest;
import vn.van.chat_service.dto.response.ChatMessageResponse;
import vn.van.chat_service.entity.ChatMessage;
import vn.van.chat_service.entity.Conversation;
import vn.van.chat_service.entity.ParticipantInfo;
import vn.van.chat_service.entity.WebSocketSession;
import vn.van.chat_service.exception.ApplicationException;
import vn.van.chat_service.mapper.ChatMessageMapper;
import vn.van.chat_service.repository.ChatMessageRepository;
import vn.van.chat_service.repository.ConversationRepository;
import vn.van.chat_service.repository.WebSocketSessionRepository;
import vn.van.chat_service.service.ChatMessageService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    WebSocketSessionRepository webSocketSessionRepository;
    ChatMessageMapper chatMessageMapper;
    SocketIOServer socketIOServer;
    ObjectMapper objectMapper;

    @Override
    public ChatMessageResponse createMessage(ChatMessageCreateRequest request) {
        ParticipantInfo sender = verifyConversationAndUser(request.getConversationId());
        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setSender(sender);
        chatMessage.setCreatedAt(Instant.now());
        chatMessage =  chatMessageRepository.save(chatMessage);

        Conversation conversation = conversationRepository.findById(request.getConversationId()).get();
        List<String> participantIds = conversation.getParticipants().stream()
                .map(ParticipantInfo::getUserId).toList();

        Map<String, WebSocketSession> sessionMap = webSocketSessionRepository.findAllByUserIdIn(participantIds).stream()
                .collect(Collectors.toMap(WebSocketSession::getSessionId, session -> session));

        ChatMessageResponse response = chatMessageMapper.toChatMessageResponse(chatMessage);
        socketIOServer.getAllClients().forEach(client -> {
            WebSocketSession session = sessionMap.get(client.getSessionId().toString());
            if (Objects.nonNull(session)) {
                String message = null;
                try {
                    response.setMe(session.getUserId().equals(extractUserId()));
                    message = objectMapper.writeValueAsString(response);
                    client.sendEvent("message", message);
                } catch (JsonProcessingException e) {
                    throw new ApplicationException(ResponseMessage.INVALID_MAPPING);
                }
            }
        });

        return toChatMessageResponse(chatMessage);
    }

    @Override
    public List<ChatMessageResponse> getMessages(String conversationId) {
        verifyConversationAndUser(conversationId);
        return chatMessageRepository.findAllByConversationIdOrderByCreatedAtDesc(conversationId)
                .stream().map(this::toChatMessageResponse).toList();
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        String currentUserId = extractUserId();
        ChatMessageResponse response = chatMessageMapper.toChatMessageResponse(chatMessage);
        response.setMe(chatMessage.getSender().getUserId().equals(currentUserId));
        return response;
    }

    private ParticipantInfo verifyConversationAndUser(String conversationId) {
        String currentUserId = extractUserId();
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.CONVERSATION_NOT_FOUND))
                .getParticipants().stream()
                .filter(participantInfo -> participantInfo.getUserId().equals(currentUserId))
                .findAny()
                .orElseThrow(() -> new ApplicationException(ResponseMessage.CONVERSATION_NOT_FOUND));
    }

    private String extractUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("user-id");
    }
}
