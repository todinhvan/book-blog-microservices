package vn.van.chat_service.repository.http_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.van.chat_service.dto.request.AuthenticationRequest;
import vn.van.chat_service.dto.response.ApiResponse;
import vn.van.chat_service.dto.response.IntrospectResponse;

@FeignClient(name = "identity-service", url = "${app.services.identity}")
public interface IdentityClient {
    @PostMapping("/auth/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody AuthenticationRequest request);
}
