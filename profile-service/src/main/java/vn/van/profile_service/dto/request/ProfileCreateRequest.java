package vn.van.profile_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreateRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
}
