package vn.van.chat_service.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(collection = "web_socket_session")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSocketSession {
    @MongoId
    String id;
    String userId;
    String sessionId;
    Instant createdAt;
}
