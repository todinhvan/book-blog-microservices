package vn.van.chat_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageCreateRequest {
    @NotBlank(message = "CHAT_CONVERSATION_ID_NOT_BLANK")
    String conversationId;

    @NotBlank(message = "CHAT_MESSAGE_NOT_BLANK")
    String message;
}
