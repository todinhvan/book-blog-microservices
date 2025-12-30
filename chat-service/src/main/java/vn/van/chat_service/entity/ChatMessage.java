package vn.van.chat_service.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(collection = "chat_message")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
// @CompoundIndexes({}): Gọp index trong cùng một collection -> index: conversationId_createdAt
public class ChatMessage {
    @MongoId
    String id;
    String message;
    ParticipantInfo sender;

    @Indexed
    String conversationId;

    @Indexed
    Instant createdAt;
}
