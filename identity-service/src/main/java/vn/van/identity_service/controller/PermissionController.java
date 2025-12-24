package vn.van.identity_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.PermissionCreateRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.PermissionResponse;
import vn.van.identity_service.service.PermissionService;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @RequestBody PermissionCreateRequest request) {
        log.info("createPermission");
        ApiResponse<PermissionResponse> response =
                toApiResponse(ResponseMessage.PERMISSION_CREATED, permissionService.createPermission(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        log.info("getAllPermissions");
        ApiResponse<List<PermissionResponse>> response =
                toApiResponse(ResponseMessage.PERMISSION_GET_ALL, permissionService.getAllPermissions());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String permissionId) {
        log.info("deletePermission");
        permissionService.deletePermission(permissionId);
        ApiResponse<Void> response = toApiResponse(ResponseMessage.PERMISSION_DELETED, null);
        return ResponseEntity.ok(response);
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
