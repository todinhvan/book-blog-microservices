package vn.van.identity_service.service;

import vn.van.identity_service.dto.request.AuthenticationRequest;
import vn.van.identity_service.dto.request.LoginRequest;
import vn.van.identity_service.dto.request.RegisterRequest;
import vn.van.identity_service.dto.response.AuthenticationResponse;
import vn.van.identity_service.dto.response.IntrospectResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    void logout(AuthenticationRequest request);

    IntrospectResponse introspect(AuthenticationRequest request);

    AuthenticationResponse refreshToken(AuthenticationRequest request);
}
