package vn.van.post_service.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document("posts")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @MongoId
    String id;
    String userId;
    String content;
    Instant createdAt;
    Instant updatedAt;
}
