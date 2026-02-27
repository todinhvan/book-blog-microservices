package vn.van.identity_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.UserCreatePasswordRequest;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserCreateRequest request) {
        log.info("createUser");
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_CREATED, userService.createUser(request)));
    }

    @GetMapping("info")
    public ResponseEntity<ApiResponse<UserResponse>> getInfo() {
        log.info("getInfo");
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_GET_INFO, userService.getInfo()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {
        log.info("getUser");
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_GET, userService.getUser(userId)));
    }

    @PostMapping("create-password")
    public ResponseEntity<ApiResponse<Void>> createPassword(@RequestBody @Valid UserCreatePasswordRequest request) {
        log.info("createPassword");
        userService.createPassword(request);
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_PASSWORD_CREATED, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        log.info("getUsers");
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_GET_ALL, userService.getAllUsers()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        log.info("updateUser");
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_UPDATED, userService.updateUser(userId, request)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        log.info("deleteUser");
        userService.deleteUser(userId);
        return ResponseEntity.ok(toApiResponse(ResponseMessage.USER_DELETED, null));
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
