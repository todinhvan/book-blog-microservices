package vn.van.notification_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.van.notification_service.constant.ResponseMessage;
import vn.van.notification_service.dto.event.NotificationEvent;
import vn.van.notification_service.dto.request.BrevoEmailRequest;
import vn.van.notification_service.dto.request.Recipient;
import vn.van.notification_service.dto.request.Sender;
import vn.van.notification_service.exception.ApplicationException;
import vn.van.notification_service.repository.http_client.BrevoEmailClient;
import vn.van.notification_service.service.NotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
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
    public void sendNotificationForWelcome(NotificationEvent event) {
        switch (event.getChannel()) {
            case "email":
                brevoEmailClient.sendMail(apiKey, BrevoEmailRequest.builder()
                        .sender(Sender.builder().email(senderEmail).name(senderName).build())
                        .to(List.of(Recipient.builder().email(event.getRecipient()).name("User").build()))
                        .subject("Welcome to Book Blog, " + event.getRecipient())
                        .htmlContent("<p>Thank you for signing up for Book Blog.</p>")
                        .build());
                break;
            default:
                throw new ApplicationException(ResponseMessage.INVALID_CHANNEL);
        }
    }
}
