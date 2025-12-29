package vn.van.file_service.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("files_management")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileManagement {
    @MongoId
    String id;
    String ownerId;
    String contentType;
    long size;
    String path;
    String md5Checksum;
}
