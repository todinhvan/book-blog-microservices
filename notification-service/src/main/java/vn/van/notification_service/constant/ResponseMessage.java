package vn.van.notification_service.constant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // Success
    SEND_MAIL_SUCCESS(HttpStatus.CREATED, "Send mail successfully"),
    // Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_KEY_RESPONSE_MESSAGE(HttpStatus.BAD_REQUEST, "Invalid Key Response"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    INVALID_CHANNEL(HttpStatus.BAD_REQUEST, "Invalid Channel"),
    // Error field

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
