package vn.van.identity_service.service;

import vn.van.identity_service.dto.request.RoleCreateRequest;
import vn.van.identity_service.dto.request.RoleUpdateRequest;
import vn.van.identity_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleCreateRequest request);
    List<RoleResponse> getAllRoles();
    RoleResponse updateRole(String roleId, RoleUpdateRequest request);
    void deleteRole(String roleId);
}
