package vn.van.identity_service.service;

import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUser(String userId);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(String userId, UserUpdateRequest request);
    void deleteUser(String userId);
}
