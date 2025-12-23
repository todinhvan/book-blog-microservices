package vn.van.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @Email(message = "LOGIN_FAILURE")
    String email;

    @NotBlank(message = "LOGIN_FAILURE")
    @Size(min = 4, message = "LOGIN_FAILURE")
    String password;
}
