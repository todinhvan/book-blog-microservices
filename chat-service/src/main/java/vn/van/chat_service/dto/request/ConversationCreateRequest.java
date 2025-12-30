package vn.van.chat_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationCreateRequest {
    String type;

    @Size(min = 1, message = "PARTICIPANT_IDS_SIZE_MIN_1")
    @NotNull(message = "PARTICIPANT_IDS_NOT_NULL")
    List<String> participantIds;
}
