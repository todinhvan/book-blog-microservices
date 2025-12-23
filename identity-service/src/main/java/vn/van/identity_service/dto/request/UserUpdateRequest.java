package vn.van.identity_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.van.identity_service.validator.DateOfBirthConstraint;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotBlank(message = "USER_FIRST_NAME_NOT_BLANK")
    String firstName;

    @NotNull(message = "USER_LAST_NAME_NOT_NULL")
    String lastName;

    @NotNull(message = "USER_DATE_OF_BIRTH_NOT_NULL")
    @DateOfBirthConstraint(minAge = 18)
    LocalDate dateOfBirth;
}
