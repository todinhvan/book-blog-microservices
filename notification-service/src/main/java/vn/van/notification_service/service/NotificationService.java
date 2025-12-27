package vn.van.notification_service.service;

import vn.van.notification_service.dto.event.NotificationEvent;

public interface NotificationService {
    void sendNotificationForWelcome(NotificationEvent event);
}
