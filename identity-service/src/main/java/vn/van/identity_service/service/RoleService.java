package vn.van.identity_service.service;

import java.util.List;

import vn.van.identity_service.dto.request.RoleCreateRequest;
import vn.van.identity_service.dto.request.RoleUpdateRequest;
import vn.van.identity_service.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleCreateRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse updateRole(String roleId, RoleUpdateRequest request);

    void deleteRole(String roleId);
}
