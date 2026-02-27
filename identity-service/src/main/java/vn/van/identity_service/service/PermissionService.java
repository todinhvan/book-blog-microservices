package vn.van.identity_service.service;

import java.util.List;

import vn.van.identity_service.dto.request.PermissionCreateRequest;
import vn.van.identity_service.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse createPermission(PermissionCreateRequest request);

    List<PermissionResponse> getAllPermissions();

    void deletePermission(String permissionId);
}
