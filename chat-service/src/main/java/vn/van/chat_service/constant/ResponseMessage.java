package vn.van.chat_service.constant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // Success
    CONVERSATION_CREATED(HttpStatus.CREATED, "Conversation Created"),
    CONVERSATION_GET_MY(HttpStatus.OK, "Conversation Get My"),
    MESSAGE_CREATED(HttpStatus.CREATED, "Message Created"),
    MESSAGE_GET(HttpStatus.OK, "Message Get My"),
    // Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_KEY_RESPONSE_MESSAGE(HttpStatus.BAD_REQUEST, "Invalid Key Response"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    CONVERSATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Conversation Not Found"),
    INVALID_MAPPING(HttpStatus.BAD_REQUEST, "Invalid Mapping"),
    // Error field
    PARTICIPANT_IDS_NOT_NULL("Create conversation: ParticipantIds must not be null"),
    PARTICIPANT_IDS_SIZE_MIN_1("Create conversation: ParticipantIds size min 1"),
    CHAT_CONVERSATION_ID_NOT_BLANK("Create chat: Conversation id must not be blank"),
    CHAT_MESSAGE_NOT_BLANK("Create chat: Message must not be blank"),
    ;

    private final int statusCode;
    private final HttpStatus status;
    private final String message;

    ResponseMessage(String message) {
        this(HttpStatus.UNPROCESSABLE_ENTITY.value(), HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    ResponseMessage(HttpStatus status, String message) {
        this(status.value(), status, message);
    }

    ResponseMessage(int statusCode, HttpStatus status, String message) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
    }
}
