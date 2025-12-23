package vn.van.identity_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.repository.UserRepository;
import vn.van.identity_service.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApplicationException(ResponseMessage.USER_EXISTED);
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setDateOfBirth(request.getDateOfBirth());
        userRepository.save(user);
        return toUserResponse(user);
    }

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
        return toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = existsUser(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        userRepository.save(user);
        return toUserResponse(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = existsUser(userId);
        userRepository.delete(user);
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setDateOfBirth(user.getDateOfBirth());
        return response;
    }

    private User existsUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
    }
}
