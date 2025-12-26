package vn.van.profile_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.van.profile_service.constant.ResponseMessage;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.response.ApiResponse;
import vn.van.profile_service.dto.response.ProfileResponse;
import vn.van.profile_service.service.ProfileService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileController {
    ProfileService profileService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> createUserProfile(@RequestBody ProfileCreateRequest request) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.PROFILE_CREATED, profileService.createProfile(request)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> getAllUserProfiles(@PathVariable String userId) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.PROFILE_GET_ALL, profileService.getAllProfiles(userId)));
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
