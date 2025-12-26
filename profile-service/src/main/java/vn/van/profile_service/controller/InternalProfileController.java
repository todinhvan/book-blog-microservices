package vn.van.profile_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        return ResponseEntity.ok(ApiResponse.<ProfileResponse>builder()
                        .statusCode(ResponseMessage.PROFILE_CREATED.getStatusCode())
                        .status(ResponseMessage.PROFILE_CREATED.getStatus())
                        .message(ResponseMessage.PROFILE_CREATED.getMessage())
                        .data(profileService.createDefault(request))
                        .build());
    }
}
