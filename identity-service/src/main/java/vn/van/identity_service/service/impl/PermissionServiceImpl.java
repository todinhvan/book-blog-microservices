package vn.van.identity_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.PermissionCreateRequest;
import vn.van.identity_service.dto.response.PermissionResponse;
import vn.van.identity_service.entity.Permission;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.mapper.PermissionMapper;
import vn.van.identity_service.repository.PermissionRepository;
import vn.van.identity_service.service.PermissionService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionCreateRequest request) {
        if (permissionRepository.existsById(request.getName())) {
            throw new ApplicationException(ResponseMessage.PERMISSION_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @Override
    public void deletePermission(String permissionId) {
        Permission permission = permissionRepository
                .findById(permissionId)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.PERMISSION_NOT_FOUND));
        permissionRepository.delete(permission);
    }
}
