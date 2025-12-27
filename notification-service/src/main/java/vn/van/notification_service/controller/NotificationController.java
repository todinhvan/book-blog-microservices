package vn.van.notification_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vn.van.notification_service.dto.event.NotificationEvent;
import vn.van.notification_service.service.NotificationService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {
    NotificationService notificationService;

    @KafkaListener(topics = "user-created")
    public void listenUserCreated(NotificationEvent event) {
        log.info("listenUserCreated");
        notificationService.sendNotificationForWelcome(event);
    }
}
