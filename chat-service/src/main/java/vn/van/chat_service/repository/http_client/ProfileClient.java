package vn.van.chat_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.van.chat_service.dto.response.ApiResponse;
import vn.van.chat_service.dto.response.ProfileResponse;

@FeignClient(name = "profile-service", url = "${app.services.profile}")
public interface ProfileClient {
    @GetMapping("/internal/info/{userId}")
    ApiResponse<ProfileResponse> info(@PathVariable String userId);
}
