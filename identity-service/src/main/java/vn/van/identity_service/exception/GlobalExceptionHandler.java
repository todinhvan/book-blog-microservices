package vn.van.identity_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error(e.toString());
        ResponseMessage responseMessage = ResponseMessage.INTERNAL_SERVER_ERROR;
        ApiResponse<?> response = toApiResponse(responseMessage);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiResponse<?>> handleApplicationException(ApplicationException e) {
        log.error(e.toString());
        ResponseMessage responseMessage = e.getResponseMessage();
        ApiResponse<?> response = toApiResponse(responseMessage);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.toString());
        ResponseMessage responseMessage = ResponseMessage.INVALID_KEY_RESPONSE_MESSAGE;
        try {
            responseMessage = ResponseMessage.valueOf(e.getFieldError().getDefaultMessage());
        } catch (IllegalArgumentException ex) {}


        ApiResponse<?> response = toApiResponse(responseMessage);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    private <T> ApiResponse<T> toApiResponse(ResponseMessage responseMessage) {
        return ApiResponse.<T>builder()
                .statusCode(responseMessage.getStatusCode())
                .status(responseMessage.getStatus())
                .message(responseMessage.getMessage())
                .build();
    }
}
