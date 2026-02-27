package vn.van.notification_service.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrevoEmailRequest {
    Sender sender;
    List<Recipient> to;
    String subject;
    String htmlContent;
}
