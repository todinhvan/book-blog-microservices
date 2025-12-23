package vn.van.identity_service.constant;

import org.springframework.http.HttpStatus;

public enum ResponseMessage {
    // Success
    USER_CREATED(HttpStatus.CREATED, "User Created"),
    USER_GET(HttpStatus.OK, "User Get"),
    USER_GET_ALL(HttpStatus.OK, "User Get All"),
    USER_UPDATED(HttpStatus.ACCEPTED, "User Updated"),
    USER_DELETED(HttpStatus.NO_CONTENT, "User Deleted"),
    // Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_EXISTED(HttpStatus.CONFLICT, "User already exists"),
    // Error field
    ;

    private int statusCode;
    private HttpStatus status;
    private String message;

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

    public int getStatusCode() {
        return statusCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
