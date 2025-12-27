package vn.van.notification_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.van.notification_service.dto.request.BrevoEmailRequest;
import vn.van.notification_service.dto.request.SendMailRequest;
import vn.van.notification_service.dto.request.Sender;
import vn.van.notification_service.dto.response.BrevoEmailResponse;
import vn.van.notification_service.repository.http_client.BrevoEmailClient;
import vn.van.notification_service.service.EmailService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    BrevoEmailClient brevoEmailClient;

    @NonFinal
    @Value("${app.config.mail.api-key}")
    String apiKey;

    @NonFinal
    @Value("${app.config.mail.sender-email}")
    String senderEmail;

    @NonFinal
    @Value("${app.config.mail.sender-name}")
    String senderName;

    @Override
    public BrevoEmailResponse sendMail(SendMailRequest request) {
        return brevoEmailClient.sendMail(apiKey, BrevoEmailRequest.builder()
                .sender(Sender.builder()
                        .email(senderEmail)
                        .name(senderName)
                        .build())
                .to(List.of(request.getRecipient()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build());
    }
}
