package vn.van.identity_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import vn.van.identity_service.dto.response.OutboundProfileResponse;

@FeignClient(name = "google-profile-service", url = "${app.services.google-oauth2.profile-uri}")
public interface OutboundProfileClient {
    @GetMapping("/oauth2/v1/userinfo")
    OutboundProfileResponse getUserProfile(
            @RequestParam("alt") String alt,
            @RequestParam("access_token") String accessToken
    );
}
