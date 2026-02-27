package vn.van.profile_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.van.profile_service.config.AuthenticationRequestInterceptor;
import vn.van.profile_service.dto.response.ApiResponse;
import vn.van.profile_service.dto.response.UserResponse;

@FeignClient(
        name = "identity-service",
        url = "${app.services.identity}",
        configuration = {AuthenticationRequestInterceptor.class}
)
public interface IdentityClient {
    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> getUser(@PathVariable String userId);
}
