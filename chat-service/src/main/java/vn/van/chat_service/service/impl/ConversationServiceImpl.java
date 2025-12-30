package vn.van.chat_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.van.chat_service.constant.ResponseMessage;
import vn.van.chat_service.dto.request.ConversationCreateRequest;
import vn.van.chat_service.dto.response.ConversationResponse;
import vn.van.chat_service.dto.response.ProfileResponse;
import vn.van.chat_service.entity.Conversation;
import vn.van.chat_service.entity.ParticipantInfo;
import vn.van.chat_service.exception.ApplicationException;
import vn.van.chat_service.mapper.ConversationMapper;
import vn.van.chat_service.mapper.ParticipantInfoMapper;
import vn.van.chat_service.repository.ConversationRepository;
import vn.van.chat_service.repository.http_client.ProfileClient;
import vn.van.chat_service.service.ConversationService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConversationServiceImpl implements ConversationService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;
    ConversationMapper conversationMapper;
    ParticipantInfoMapper participantInfoMapper;

    @Override
    public List<ConversationResponse> myConversations() {
        String currentUserId = extractUserId();
        log.info(currentUserId);
        List<Conversation> conversations = conversationRepository.findAllByParticipantUserId(currentUserId);
        return conversations.stream().map(this::toConversationResponse).toList();
    }

    @Override
    public ConversationResponse create(ConversationCreateRequest request) {
        var userData = profileClient.info(extractUserId());
        var participantData = profileClient.info(request.getParticipantIds().getFirst());
        if (Objects.isNull(userData) || Objects.isNull(participantData)) {
            throw new ApplicationException(ResponseMessage.USER_NOT_FOUND);
        }
        ProfileResponse user = userData.getData();
        ProfileResponse participant = participantData.getData();

        List<String> participantIds = new ArrayList<>();
        participantIds.add(user.getId());
        participantIds.add(participant.getId());
        String participantHash = generateParticipantHash(participantIds.stream().sorted().toList());

        Conversation conversation = conversationRepository.findByParticipantsHash(participantHash)
                .orElseGet(() -> {
                    List<ParticipantInfo> participants = new ArrayList<>();
                    participants.add(participantInfoMapper.toParticipantInfo(user));
                    participants.add(participantInfoMapper.toParticipantInfo(participant));

                    Conversation newConversation = new Conversation();
                    newConversation.setType(request.getType());
                    newConversation.setParticipants(participants);
                    newConversation.setParticipantsHash(participantHash);
                    newConversation.setCreatedAt(Instant.now());
                    newConversation.setUpdatedAt(Instant.now());
                    return conversationRepository.save(newConversation);
                });

        return toConversationResponse(conversation);
    }

    private String generateParticipantHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String currentUserId = extractUserId();
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);

        conversation.getParticipants().stream()
                .filter(participantInfo -> !participantInfo.getUserId().equals(currentUserId))
                .findFirst().ifPresent(participantInfo -> {
                    conversationResponse.setConversationName(participantInfo.getEmail());
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                });

        return conversationResponse;
    }

    private String extractUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("user-id");
    }
}
