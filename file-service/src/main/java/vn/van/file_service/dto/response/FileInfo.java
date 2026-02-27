package vn.van.file_service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileInfo {
    String name;
    String contentType;
    long size;
    String path;
    String md5Checksum;
    String url;
}
