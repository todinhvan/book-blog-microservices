package vn.van.identity_service.dto.keycloak.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeClientTokenRequest {
    String grant_type;
    String client_id;
    String client_secret;
    String scope;
}
