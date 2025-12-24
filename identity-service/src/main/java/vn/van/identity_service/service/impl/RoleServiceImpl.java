package vn.van.identity_service.service.impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.RoleCreateRequest;
import vn.van.identity_service.dto.request.RoleUpdateRequest;
import vn.van.identity_service.dto.response.RoleResponse;
import vn.van.identity_service.entity.Role;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.mapper.RoleMapper;
import vn.van.identity_service.repository.PermissionRepository;
import vn.van.identity_service.repository.RoleRepository;
import vn.van.identity_service.service.RoleService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleCreateRequest request) {
        if (roleRepository.existsById(request.getName())) {
            throw new ApplicationException(ResponseMessage.ROLE_EXISTED);
        }

        Role role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(request.getPermissions())));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse updateRole(String roleId, RoleUpdateRequest request) {
        Role role = existsRole(roleId);
        roleMapper.updateRole(role, request);
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(request.getPermissions())));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = existsRole(roleId);
        roleRepository.delete(role);
    }

    private Role existsRole(String roleId) {
        return roleRepository
                .findById(roleId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.ROLE_NOT_FOUND));
    }
}
