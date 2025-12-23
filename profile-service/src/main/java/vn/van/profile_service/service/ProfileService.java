package vn.van.profile_service.service;

import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.response.ProfileResponse;

import java.util.List;

public interface ProfileService {
    ProfileResponse createProfile(ProfileCreateRequest request);
    List<ProfileResponse> getAllProfiles(String userId);
}
