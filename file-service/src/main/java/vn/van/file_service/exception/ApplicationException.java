package vn.van.file_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import vn.van.file_service.constant.ResponseMessage;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {
    ResponseMessage responseMessage;

    public ApplicationException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }
}