package vn.van.identity_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.RoleCreateRequest;
import vn.van.identity_service.dto.request.RoleUpdateRequest;
import vn.van.identity_service.dto.response.ApiResponse;
import vn.van.identity_service.dto.response.RoleResponse;
import vn.van.identity_service.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleCreateRequest request) {
        log.info("createRole");
        ApiResponse<RoleResponse> response = toApiResponse(ResponseMessage.ROLE_CREATED, roleService.createRole(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        log.info("getAllRoles");
        ApiResponse<List<RoleResponse>> response = toApiResponse(ResponseMessage.ROLE_GET_ALL,roleService.getAllRoles());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(@PathVariable String roleId, @RequestBody RoleUpdateRequest request) {
        log.info("updateRole");
        ApiResponse<RoleResponse> response = toApiResponse(ResponseMessage.ROLE_UPDATED, roleService.updateRole(roleId, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String roleId) {
        log.info("deleteRole");
        roleService.deleteRole(roleId);
        ApiResponse<Void> response = toApiResponse(ResponseMessage.ROLE_DELETED, null);
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
