package vn.van.identity_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.AuthenticationRequest;
import vn.van.identity_service.dto.request.LoginRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.AuthenticationResponse;
import vn.van.identity_service.dto.response.IntrospectResponse;
import vn.van.identity_service.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        log.info("login");
        ApiResponse<AuthenticationResponse> response =
                toApiResponse(ResponseMessage.LOGIN_SUCCESS, authenticationService.login(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody AuthenticationRequest request) {
        log.info("logout");
        authenticationService.logout(request);
        ApiResponse<Void> response = toApiResponse(ResponseMessage.LOGOUT_SUCCESS, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody AuthenticationRequest request) {
        log.info("introspect");
        ApiResponse<IntrospectResponse> response =
                toApiResponse(ResponseMessage.INTROSPECT_SUCCESS, authenticationService.introspect(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @RequestBody AuthenticationRequest request) {
        log.info("refresh-token");
        ApiResponse<AuthenticationResponse> response =
                toApiResponse(ResponseMessage.REFRESH_TOKEN_SUCCESS, authenticationService.refreshToken(request));
        return ResponseEntity.ok(response);
    }

    private <T> ApiResponse<T> toApiResponse(ResponseMessage responseMessage, T data) {
        return ApiResponse.<T>builder()
                .statusCode(responseMessage.getStatusCode())
                .status(responseMessage.getStatus())
                .message(responseMessage.getMessage())
                .data(data)
                .build();
    }
}
