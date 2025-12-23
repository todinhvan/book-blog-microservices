package vn.van.profile_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.response.ProfileResponse;
import vn.van.profile_service.entity.Profile;
import vn.van.profile_service.mapper.ProfileMapper;
import vn.van.profile_service.repository.ProfileRepository;
import vn.van.profile_service.service.ProfileService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    @Override
    public ProfileResponse createProfile(ProfileCreateRequest request) {
        if (!existsUser(request.getUserId())) {
            throw new RuntimeException("User not found");
        }

        Profile profile = profileMapper.toProfile(request);
        profileRepository.save(profile);
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public List<ProfileResponse> getAllProfiles(String userId) {
        if (!existsUser(userId)) {
            throw new RuntimeException("User not found");
        }
        return profileRepository.findAllByUserId(userId).stream().map(profileMapper::toProfileResponse).toList();
    }

    // hard code to Test
    private boolean existsUser(String userId) {
        return true;
    }
}
