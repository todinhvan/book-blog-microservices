package vn.van.identity_service.mapper;

import org.mapstruct.Mapper;

import vn.van.identity_service.dto.request.ProfileCreateRequest;
import vn.van.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreateRequest toProfileCreateRequest(User user);
}
