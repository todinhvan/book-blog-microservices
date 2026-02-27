package vn.van.profile_service.service;

import org.springframework.web.multipart.MultipartFile;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.request.ProfileUpdateRequest;
import vn.van.profile_service.dto.request.SearchUserRequest;
import vn.van.profile_service.dto.response.ProfileResponse;

import java.util.List;

public interface ProfileService {
    ProfileResponse createProfile(ProfileCreateRequest request);
    List<ProfileResponse> getAllProfiles(String userId);
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileUpdateRequest request);
    ProfileResponse changeAvatar(MultipartFile file);
    List<ProfileResponse> search(SearchUserRequest request);
}
