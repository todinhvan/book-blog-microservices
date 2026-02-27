package vn.van.identity_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.van.identity_service.dto.request.RoleCreateRequest;
import vn.van.identity_service.dto.request.RoleUpdateRequest;
import vn.van.identity_service.dto.response.RoleResponse;
import vn.van.identity_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreateRequest request);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest request);

    RoleResponse toRoleResponse(Role role);
}
