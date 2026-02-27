package vn.van.notification_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vn.van.notification_service.dto.request.BrevoEmailRequest;
import vn.van.notification_service.dto.response.BrevoEmailResponse;

@FeignClient(name = "brevo-mail-service", url = "https://api.brevo.com")
public interface BrevoEmailClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    BrevoEmailResponse sendMail(@RequestHeader("api-key") String apiKey, @RequestBody BrevoEmailRequest request);
}
