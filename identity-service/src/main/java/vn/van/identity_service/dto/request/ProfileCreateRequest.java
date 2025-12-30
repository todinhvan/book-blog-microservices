package vn.van.identity_service.dto.request;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreateRequest {
    String userId;
    String email;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
}
