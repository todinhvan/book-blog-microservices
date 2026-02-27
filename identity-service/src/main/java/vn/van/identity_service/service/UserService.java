package vn.van.identity_service.service;

import java.util.List;

import vn.van.identity_service.dto.request.UserCreatePasswordRequest;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    UserResponse getInfo();

    void createPassword(UserCreatePasswordRequest request);

    UserResponse getUser(String userId);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);
}
