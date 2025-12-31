package vn.van.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreatePasswordRequest {
    @NotBlank(message = "USER_PASSWORD_INVALID")
    @Size(min = 4, message = "USER_PASSWORD_INVALID")
    String password;
}
