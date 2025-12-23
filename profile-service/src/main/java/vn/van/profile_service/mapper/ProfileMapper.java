package vn.van.profile_service.mapper;

import org.mapstruct.Mapper;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.response.ProfileResponse;
import vn.van.profile_service.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreateRequest request);
    ProfileResponse toProfileResponse(Profile profile);
}
