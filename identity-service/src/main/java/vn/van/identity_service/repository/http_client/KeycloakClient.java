package vn.van.identity_service.repository.http_client;

import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import vn.van.identity_service.dto.keycloak.request.ExchangeClientTokenRequest;
import vn.van.identity_service.dto.keycloak.request.UserCreateRequest;
import vn.van.identity_service.dto.keycloak.response.ExchangeClientTokenResponse;

@FeignClient(name = "keycloak-service", url = "${app.services.keycloak.uri}")
public interface KeycloakClient {
    @PostMapping(
            value = "/realms/book-blog/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ExchangeClientTokenResponse exchangeToken(@QueryMap ExchangeClientTokenRequest request);

    @PostMapping(
            value = "/admin/realms/book-blog/users",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody UserCreateRequest request
    );
}
