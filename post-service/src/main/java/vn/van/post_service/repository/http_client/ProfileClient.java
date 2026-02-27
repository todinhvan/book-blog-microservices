package vn.van.post_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.van.post_service.dto.response.ApiResponse;
import vn.van.post_service.dto.response.ProfileResponse;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile}",
        configuration = {

        }
)
public interface ProfileClient {
    @GetMapping(value = "/internal/info/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProfileResponse> getProfile(@PathVariable String userId);
}
