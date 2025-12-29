package vn.van.profile_service.constant;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    // Success
    PROFILE_CREATED(HttpStatus.CREATED, "Profile Created"),
    PROFILE_GET_ALL(HttpStatus.OK, "Profile Get All"),
    PROFILE_GET(HttpStatus.OK, "Profile Get"),
    PROFILE_UPDATED(HttpStatus.OK, "Profile Updated"),
    PROFILE_CHANGE_AVATAR(HttpStatus.OK, "Profile Change Avatar"),
    // Error
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_KEY_RESPONSE_MESSAGE(HttpStatus.BAD_REQUEST, "Invalid Key"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile Not Found"),
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
