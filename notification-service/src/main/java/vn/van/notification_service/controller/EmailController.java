package vn.van.notification_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.van.notification_service.constant.ResponseMessage;
import vn.van.notification_service.dto.request.SendMailRequest;
import vn.van.notification_service.dto.response.ApiResponse;
import vn.van.notification_service.dto.response.BrevoEmailResponse;
import vn.van.notification_service.service.EmailService;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<BrevoEmailResponse>> send(@RequestBody SendMailRequest request) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.SEND_MAIL_SUCCESS, emailService.sendMail(request)));
    }

    private <T> ApiResponse<T> toApiResponse(ResponseMessage responseMessage, T data) {
        return ApiResponse.<T>builder()
                .statusCode(responseMessage.getStatusCode())
                .status(responseMessage.getStatus())
                .message(responseMessage.getMessage())
                .data(data)
                .build();
    }
}
