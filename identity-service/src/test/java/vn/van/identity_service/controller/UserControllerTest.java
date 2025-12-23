package vn.van.identity_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.UserCreateRequest;
import vn.van.identity_service.dto.response.UserResponse;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.service.UserService;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    // Data
    private String userId = "abc";
    private String email = "test@gmail.com";
    private String password = "12345678";
    private String firstName = "Test";
    private String lastName = "Test";
    private LocalDate dateOfBirth = LocalDate.of(2003, 8, 26);
    private String city = "London";

    private UserCreateRequest request;
    private UserResponse userResponse;

    @BeforeEach
    void initData() {
        request = new UserCreateRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPassword(password);
        request.setCity(city);
        request.setDateOfBirth(dateOfBirth);

        userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setFirstName(firstName);
        userResponse.setLastName(lastName);
        userResponse.setEmail(email);
        userResponse.setDateOfBirth(dateOfBirth);
        userResponse.setCity(city);
    }

    @Test
    public void createUser_success() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content))
        // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(userId));
    }

    @Test
    public void createUser_fail_with_user_exists() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenThrow(new ApplicationException(ResponseMessage.USER_EXISTED));

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
        // Then
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User already exists"));
    }

    @Test
    public void createUser_fail_with_password_invalid() throws Exception {
        // Given
        request.setPassword("123");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                // Then
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("statusCode").value(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User: Password must be greater than or equals to 4 characters"));
    }
}
