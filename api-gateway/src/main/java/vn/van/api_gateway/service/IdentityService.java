package vn.van.api_gateway.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.van.api_gateway.dto.request.AuthenticationRequest;
import vn.van.api_gateway.dto.response.ApiResponse;
import vn.van.api_gateway.dto.response.IntrospectResponse;
import vn.van.api_gateway.repository.IdentityClient;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setToken(token);
        return identityClient.introspect(request);
    }
}
