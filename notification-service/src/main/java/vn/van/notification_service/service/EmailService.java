package vn.van.notification_service.service;

import vn.van.notification_service.dto.request.SendMailRequest;
import vn.van.notification_service.dto.response.BrevoEmailResponse;

public interface EmailService {
    BrevoEmailResponse sendMail(SendMailRequest request);
}
