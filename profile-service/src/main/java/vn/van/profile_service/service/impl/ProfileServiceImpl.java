package vn.van.profile_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.van.profile_service.constant.ResponseMessage;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.request.ProfileUpdateRequest;
import vn.van.profile_service.dto.request.SearchUserRequest;
import vn.van.profile_service.dto.response.FileResponse;
import vn.van.profile_service.dto.response.ProfileResponse;
import vn.van.profile_service.dto.response.UserResponse;
import vn.van.profile_service.entity.Profile;
import vn.van.profile_service.exception.ApplicationException;
import vn.van.profile_service.mapper.ProfileMapper;
import vn.van.profile_service.repository.ProfileRepository;
import vn.van.profile_service.repository.http_client.FileClient;
import vn.van.profile_service.repository.http_client.IdentityClient;
import vn.van.profile_service.service.ProfileService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    ProfileRepository profileRepository;
    IdentityClient identityClient;
    FileClient fileClient;
    ProfileMapper profileMapper;

    @Override
//    @PreAuthorize("hasAuthority('CREATE_PROFILE')")
    public ProfileResponse createProfile(ProfileCreateRequest request) {
        UserResponse user = verifyUser(request.getUserId());
        Profile profile = profileMapper.toProfile(request);
        profile.setEmail(user.getEmail());
        profile = profileRepository.save(profile);
        return profileMapper.toProfileResponse(profile);
    }

    @Override
//    @PreAuthorize("hasAuthority('GET_ALL')")
    public List<ProfileResponse> getAllProfiles(String userId) {
        verifyUser(userId);
        return profileRepository.findAllByUserId(userId).stream().map(profileMapper::toProfileResponse).toList();
    }

    @Override
    public ProfileResponse getProfile() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdInToken = jwt.getClaimAsString("user-id");
        Profile profile = profileRepository.findByUserId(userIdInToken)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdInToken = jwt.getClaimAsString("user-id");

        Profile profile = profileRepository.findByUserId(userIdInToken)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
        profileMapper.updateProfile(profile, request);
        profile = profileRepository.save(profile);
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public ProfileResponse changeAvatar(MultipartFile file) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdInToken = jwt.getClaimAsString("user-id");

        Profile profile = profileRepository.findByUserId(userIdInToken)
                .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
        FileResponse fileResponse = fileClient.uploadFile(file).getData();
        profile.setAvatar(fileResponse.getUrl());

        profile = profileRepository.save(profile);
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public List<ProfileResponse> search(SearchUserRequest request) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdInToken = jwt.getClaimAsString("user-id");

        List<Profile> profiles = profileRepository.findAllByEmailLike(request.getKeyword());
        return profiles.stream()
                .filter(profile -> !profile.getId().equals(userIdInToken))
                .map(profileMapper::toProfileResponse)
                .toList();
    }

    private UserResponse verifyUser(String userId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdInToken = jwt.getClaimAsString("user-id");
        if (!StringUtils.hasText(userIdInToken)) {
            throw new ApplicationException(ResponseMessage.UNAUTHORIZED);
        }

        log.info("user-id: {}", userId);
        log.info("user-id in token: {}", userIdInToken);

        if (!userId.equals(userIdInToken)) {
            throw new ApplicationException(ResponseMessage.FORBIDDEN);
        }

        UserResponse result = identityClient.getUser(userId).getData();
        if (!result.getId().equals(userId)) {
            throw new ApplicationException(ResponseMessage.USER_NOT_FOUND);
        }
        log.info(result.toString());
        return result;
    }
}
