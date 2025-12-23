package vn.van.identity_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        ResponseMessage responseMessage = ResponseMessage.INTERNAL_SERVER_ERROR;
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatusCode(responseMessage.getStatusCode());
        response.setStatus(responseMessage.getStatus());
        response.setMessage(responseMessage.getMessage());
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiResponse<?>> handleApplicationException(ApplicationException e) {
        ResponseMessage responseMessage = e.getResponseMessage();
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatusCode(responseMessage.getStatusCode());
        response.setStatus(responseMessage.getStatus());
        response.setMessage(responseMessage.getMessage());
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }
}
