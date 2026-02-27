package vn.van.identity_service.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.ProfileResponse;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.entity.Role;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.repository.RoleRepository;
import vn.van.identity_service.repository.UserRepository;
import vn.van.identity_service.repository.http_client.ProfileClient;

@SpringBootTest
@TestPropertySource("/test.properties")
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private ProfileClient profileClient;

    // Data
    private String userId = "abc";
    private String email = "test@gmail.com";
    private String password = "12345678";
    private String firstName = "Test";
    private String lastName = "Test";
    private LocalDate dateOfBirth = LocalDate.of(2003, 8, 26);
    private String city = "London";

    private UserCreateRequest request;
    private User user;
    private Role role;
    private ProfileResponse responseProfile;

    @BeforeEach
    void initData() {
        request = new UserCreateRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPassword(password);
        request.setCity(city);
        request.setDateOfBirth(dateOfBirth);

        user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setDateOfBirth(dateOfBirth);
        user.setCity(city);

        role = new Role();
        role.setName("USER");
        role.setDescription("");

        responseProfile = new ProfileResponse();
        responseProfile.setId("profile");
        responseProfile.setUserId(userId);
        responseProfile.setFirstName(firstName);
        responseProfile.setLastName(lastName);
        responseProfile.setDateOfBirth(dateOfBirth);
        responseProfile.setCity(city);
    }

    @BeforeEach
    void setupSecurityContext() {
        var auth = new UsernamePasswordAuthenticationToken(
                email, password, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void createUser_success() {
        // Given
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(profileClient.createDefaultProfile(Mockito.any())).thenReturn(ApiResponse.<ProfileResponse>builder()
                        .statusCode(ResponseMessage.PROFILE_CREATED.getStatusCode())
                        .status(ResponseMessage.PROFILE_CREATED.getStatus())
                        .message(ResponseMessage.PROFILE_CREATED.getMessage())
                        .data(responseProfile)
                        .build());

        // When
        UserResponse userResponse = userService.createUser(request);

        // Then
        Assertions.assertThat(userResponse).isNotNull();
        Assertions.assertThat(userResponse.getId()).isEqualTo(userId);
        Assertions.assertThat(userResponse.getEmail()).isEqualTo(email);
    }

    @Test
    public void createUser_fail_with_userExisted() {
        // Given
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any()))
                .thenThrow(new DataIntegrityViolationException("User already exists"));

        // When
        ApplicationException e = assertThrows(ApplicationException.class, () -> userService.createUser(request));

        // Then
        Assertions.assertThat(e.getResponseMessage().getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        Assertions.assertThat(e.getResponseMessage().getStatus()).isEqualTo(HttpStatus.CONFLICT);
        Assertions.assertThat(e.getResponseMessage().getMessage()).isEqualTo("User already exists");
    }

    @Test
    public void createUser_fail_with_roleNotFound() {
        // Given
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        // When
        ApplicationException e = assertThrows(ApplicationException.class, () -> userService.createUser(request));

        // Then
        Assertions.assertThat(e.getResponseMessage().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        Assertions.assertThat(e.getResponseMessage().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(e.getResponseMessage().getMessage()).isEqualTo("Role not found");
    }
}
