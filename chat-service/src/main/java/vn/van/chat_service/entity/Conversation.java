package vn.van.chat_service.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Document(collection = "conversation")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation {
    @MongoId
    String id;
    String type; // GROUP, DIRECT
    List<ParticipantInfo> participants;
    Instant createdAt;
    Instant updatedAt;

    @Indexed(unique = true)
    String participantsHash;
}
