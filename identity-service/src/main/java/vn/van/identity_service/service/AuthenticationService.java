package vn.van.identity_service.service;

import com.nimbusds.jose.JOSEException;
import vn.van.identity_service.dto.request.LoginRequest;
import vn.van.identity_service.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request) throws JOSEException;
}
