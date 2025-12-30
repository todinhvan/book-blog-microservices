package vn.van.chat_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.van.chat_service.entity.ParticipantInfo;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    String type; // GROUP, DIRECT
    String participantsHash;
    String conversationAvatar;
    String conversationName;
    List<ParticipantInfo> participants;
    Instant createdAt;
    Instant updatedAt;
}
