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
import vn.van.profile_service.service.impl.InternalProfileService;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalProfileController {
    InternalProfileService profileService;

    @PostMapping("create")
    public ResponseEntity<ApiResponse<ProfileResponse>> createDefaultProfile(@RequestBody ProfileCreateRequest request) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.PROFILE_CREATED, profileService.createDefault(request)));
    }

    @GetMapping("info/{userId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.PROFILE_GET, profileService.getProfile(userId)));
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
