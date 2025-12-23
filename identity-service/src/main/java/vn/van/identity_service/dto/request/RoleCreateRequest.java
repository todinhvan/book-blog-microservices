package vn.van.identity_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateRequest {
    String name;
    String description;
    Set<String> permissions;
}
