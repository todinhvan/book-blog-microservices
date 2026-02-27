package vn.van.chat_service.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import vn.van.chat_service.constant.ResponseMessage;
import vn.van.chat_service.dto.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error(e.toString());
        ResponseMessage responseMessage = ResponseMessage.INTERNAL_SERVER_ERROR;
        ApiResponse<?> response = toApiResponse(responseMessage, null);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiResponse<?>> handleApplicationException(ApplicationException e) {
        log.error(e.toString());
        ResponseMessage responseMessage = e.getResponseMessage();
        ApiResponse<?> response = toApiResponse(responseMessage, null);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.toString());
        ResponseMessage responseMessage = ResponseMessage.INVALID_KEY_RESPONSE_MESSAGE;
        String message = responseMessage.getMessage();

        try {
            responseMessage = ResponseMessage.valueOf(e.getFieldError().getDefaultMessage());
            var violation = e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            var attributes = violation.getConstraintDescriptor().getAttributes();
            message = mapCustomMessage(responseMessage.getMessage(), attributes);
        } catch (IllegalArgumentException ex) {
        }

        ApiResponse<?> response = toApiResponse(responseMessage, message);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.toString());
        ResponseMessage responseMessage = ResponseMessage.FORBIDDEN;
        ApiResponse<?> response = toApiResponse(responseMessage, null);
        return new ResponseEntity<>(response, responseMessage.getStatus());
    }

    private <T> ApiResponse<T> toApiResponse(ResponseMessage responseMessage, String customMessage) {
        return ApiResponse.<T>builder()
                .statusCode(responseMessage.getStatusCode())
                .status(responseMessage.getStatus())
                .message(Objects.isNull(customMessage) ? responseMessage.getMessage() : customMessage)
                .build();
    }

    private String mapCustomMessage(String message, Map<String, Object> attributes) {
        return attributes.containsKey("minAge")
                ? message.replace("{minAge}", attributes.get("minAge").toString())
                : message;
    }
}
