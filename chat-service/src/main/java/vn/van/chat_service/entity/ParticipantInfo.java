package vn.van.chat_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantInfo {
    String userId;
    String email;
    String firstName;
    String lastName;
    String avatar;
}
