package vn.van.identity_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.context.annotation.Profile;
import vn.van.identity_service.dto.request.ProfileCreateRequest;
import vn.van.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreateRequest  toProfileCreateRequest(User user);
}
