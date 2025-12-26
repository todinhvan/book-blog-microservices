package vn.van.profile_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.response.ProfileResponse;
import vn.van.profile_service.entity.Profile;
import vn.van.profile_service.mapper.ProfileMapper;
import vn.van.profile_service.repository.ProfileRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class InternalProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public ProfileResponse createDefault(@RequestBody ProfileCreateRequest request) {
        Profile profile = profileMapper.toProfile(request);
        return profileMapper.toProfileResponse(profileRepository.save(profile));
    }
}
