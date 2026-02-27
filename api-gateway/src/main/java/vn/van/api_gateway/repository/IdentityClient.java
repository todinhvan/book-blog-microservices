package vn.van.api_gateway.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;
import vn.van.api_gateway.dto.request.AuthenticationRequest;
import vn.van.api_gateway.dto.response.ApiResponse;
import vn.van.api_gateway.dto.response.IntrospectResponse;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody AuthenticationRequest request);
}
