package vn.van.chat_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.van.chat_service.constant.ResponseMessage;
import vn.van.chat_service.dto.request.ChatMessageCreateRequest;
import vn.van.chat_service.dto.response.ChatMessageResponse;
import vn.van.chat_service.entity.ChatMessage;
import vn.van.chat_service.entity.ParticipantInfo;
import vn.van.chat_service.exception.ApplicationException;
import vn.van.chat_service.mapper.ChatMessageMapper;
import vn.van.chat_service.repository.ChatMessageRepository;
import vn.van.chat_service.repository.ConversationRepository;
import vn.van.chat_service.service.ChatMessageService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageServiceImpl implements ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    ChatMessageMapper chatMessageMapper;

    @Override
    public ChatMessageResponse createMessage(ChatMessageCreateRequest request) {
        ParticipantInfo sender = verifyConversationAndUser(request.getConversationId());
        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setSender(sender);
        chatMessage.setCreatedAt(Instant.now());
        chatMessage =  chatMessageRepository.save(chatMessage);
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
