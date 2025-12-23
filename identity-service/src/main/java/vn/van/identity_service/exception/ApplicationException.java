package vn.van.identity_service.exception;

import vn.van.identity_service.constant.ResponseMessage;

public class ApplicationException extends RuntimeException {
    private ResponseMessage responseMessage;

    public ApplicationException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }
}
