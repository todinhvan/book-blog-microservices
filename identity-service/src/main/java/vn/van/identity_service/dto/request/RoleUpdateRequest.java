package vn.van.identity_service.dto.request;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {
    String description;
    Set<String> permissions;
}
