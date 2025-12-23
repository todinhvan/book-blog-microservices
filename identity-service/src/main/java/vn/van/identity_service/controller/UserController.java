package vn.van.identity_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{userId}")
    public UserResponse  getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}
