package vn.van.identity_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import vn.van.identity_service.dto.request.ProfileCreateRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.ProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @PostMapping(value = "/internal/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProfileResponse> createDefaultProfile(@RequestBody ProfileCreateRequest request);
}
