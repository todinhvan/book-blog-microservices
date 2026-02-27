package vn.van.identity_service.repository.http_client;

import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import vn.van.identity_service.dto.request.ExchangeTokenRequest;
import vn.van.identity_service.dto.response.ExchangeTokenResponse;

@FeignClient(name = "google-oauth2-service", url = "${app.services.google-oauth2.token-uri}")
public interface OutboundAuthenticateClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
