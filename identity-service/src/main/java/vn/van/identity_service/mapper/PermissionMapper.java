package vn.van.identity_service.mapper;

import org.mapstruct.Mapper;
import vn.van.identity_service.dto.request.PermissionCreateRequest;
import vn.van.identity_service.dto.response.PermissionResponse;
import vn.van.identity_service.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreateRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
