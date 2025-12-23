package vn.van.identity_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity
                .ok(toApiResponse(ResponseMessage.USER_CREATED, userService.createUser(request)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {
        return ResponseEntity
                .ok(toApiResponse(ResponseMessage.USER_GET, userService.getUser(userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        return ResponseEntity
                .ok(toApiResponse(ResponseMessage.USER_GET_ALL, userService.getAllUsers()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ResponseEntity
                .ok(toApiResponse(ResponseMessage.USER_UPDATED, userService.updateUser(userId, request)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity
                .ok(toApiResponse(ResponseMessage.USER_DELETED, null));
    }

    private <T> ApiResponse<T> toApiResponse(ResponseMessage responseMessage, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatusCode(responseMessage.getStatusCode());
        response.setStatus(responseMessage.getStatus());
        response.setMessage(responseMessage.getMessage());
        response.setData(data);
        return response;
    }
}
