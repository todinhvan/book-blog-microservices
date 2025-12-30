package vn.van.chat_service.constant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // Success
    CONVERSATION_CREATED(HttpStatus.CREATED, "Conversation Created"),
    CONVERSATION_GET_MY(HttpStatus.OK, "Conversation Get My"),
    // Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_KEY_RESPONSE_MESSAGE(HttpStatus.BAD_REQUEST, "Invalid Key Response"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    // Error field
    PARTICIPANT_IDS_NOT_NULL("Create conversation: ParticipantIds cannot be null"),
    PARTICIPANT_IDS_SIZE_MIN_1("Create conversation: ParticipantIds size min 1"),
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
