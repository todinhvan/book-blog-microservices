package vn.van.identity_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "USER_FIRST_NAME_NOT_BLANK")
    String firstName;

    @NotNull(message = "USER_LAST_NAME_NOT_NULL")
    String lastName;

    @Email(message = "USER_EMAIL_INVALID")
    String email;

    @NotBlank(message = "USER_PASSWORD_INVALID")
    @Size(min = 4, message = "USER_PASSWORD_INVALID")
    String password;

    @NotNull(message = "USER_DATE_OF_BIRTH_INVALID")
    LocalDate dateOfBirth;
}
