package vn.van.identity_service.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.constant.RoleType;
import vn.van.identity_service.dto.request.ProfileCreateRequest;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.request.UserUpdateRequest;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.mapper.ProfileMapper;
import vn.van.identity_service.mapper.UserMapper;
import vn.van.identity_service.repository.RoleRepository;
import vn.van.identity_service.repository.UserRepository;
import vn.van.identity_service.repository.http_client.ProfileClient;
import vn.van.identity_service.service.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    ProfileClient profileClient;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATE')")
    public UserResponse createUser(UserCreateRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toUser(request);
        user.setRoles(Set.of(roleRepository
                .findById(RoleType.USER.name())
                .orElseThrow(() -> new ApplicationException(ResponseMessage.ROLE_NOT_FOUND))));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ResponseMessage.USER_EXISTED);
        }

        ProfileCreateRequest profileCreateRequest = profileMapper.toProfileCreateRequest(user);
        profileCreateRequest.setUserId(user.getId());
        var result = profileClient.createProfile(profileCreateRequest);
        log.info(result.toString());

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GET')")
    public UserResponse getUser(String userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GET_ALL')")
    public List<UserResponse> getAllUsers() {
        Authentication context = SecurityContextHolder.getContext().getAuthentication();
        context.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.toString()));
        Jwt jwt = (Jwt) context.getPrincipal();
        log.info(jwt.getClaimAsString("user-id"));

        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE')")
    //    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = existsUser(userId);
        userMapper.updateUser(user, request);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DELETE')")
    public void deleteUser(String userId) {
        User user = existsUser(userId);
        userRepository.delete(user);
    }

    private User existsUser(String userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
    }
}
