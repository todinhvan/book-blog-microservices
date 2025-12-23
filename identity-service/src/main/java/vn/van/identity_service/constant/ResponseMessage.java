package vn.van.identity_service.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseMessage {
    // Success
    USER_CREATED(HttpStatus.CREATED, "User Created"),
    USER_GET(HttpStatus.OK, "User Get"),
    USER_GET_ALL(HttpStatus.OK, "User Get All"),
    USER_UPDATED(HttpStatus.ACCEPTED, "User Updated"),
    USER_DELETED(HttpStatus.NO_CONTENT, "User Deleted"),
    ROLE_CREATED(HttpStatus.CREATED, "Role Created"),
    ROLE_GET_ALL(HttpStatus.OK, "Role Get All"),
    ROLE_UPDATED(HttpStatus.ACCEPTED, "Role Updated"),
    ROLE_DELETED(HttpStatus.NO_CONTENT, "Role Deleted"),
    PERMISSION_CREATED(HttpStatus.CREATED, "Permission Created"),
    PERMISSION_GET_ALL(HttpStatus.OK, "Permission Get All"),
    PERMISSION_DELETED(HttpStatus.NO_CONTENT, "Permission Deleted"),
    LOGIN_SUCCESS(HttpStatus.OK, "Login Success"),
    // Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_KEY_RESPONSE_MESSAGE(HttpStatus.BAD_REQUEST, "Invalid Key Response"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_EXISTED(HttpStatus.CONFLICT, "User already exists"),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Role not found"),
    ROLE_EXISTED(HttpStatus.CONFLICT, "Role already exists"),
    PERMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "Permission not found"),
    PERMISSION_EXISTED(HttpStatus.CONFLICT, "Permission already exists"),
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "Email or password incorrect"),
    // Error field
    USER_FIRST_NAME_NOT_BLANK("User: First name is not empty"),
    USER_LAST_NAME_NOT_NULL("User: Last name is require"),
    USER_EMAIL_INVALID("User: Email is invalid"),
    USER_PASSWORD_INVALID("User: Password must be greater than or equals to 4 characters"),
    USER_DATE_OF_BIRTH_NOT_NULL("User: Date of birth is require (YYYY-MM-DD)"),
    USER_DATE_OF_BIRTH_INVALID("User: Date of birth must be greater than or equals {minAge} years old")

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
