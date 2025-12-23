package vn.van.identity_service.service;

import vn.van.identity_service.dto.request.PermissionCreateRequest;
import vn.van.identity_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionCreateRequest request);
    List<PermissionResponse> getAllPermissions();
    void deletePermission(String permissionId);
}
