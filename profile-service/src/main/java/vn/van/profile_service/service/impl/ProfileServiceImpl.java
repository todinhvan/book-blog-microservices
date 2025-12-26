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
import vn.van.profile_service.constant.ResponseMessage;
import vn.van.profile_service.dto.request.ProfileCreateRequest;
import vn.van.profile_service.dto.response.ProfileResponse;
import vn.van.profile_service.dto.response.UserResponse;
import vn.van.profile_service.entity.Profile;
import vn.van.profile_service.exception.ApplicationException;
import vn.van.profile_service.mapper.ProfileMapper;
import vn.van.profile_service.repository.ProfileRepository;
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
    ProfileMapper profileMapper;

    @Override
    @PreAuthorize("hasAuthority('CREATE_PROFILE')")
    public ProfileResponse createProfile(ProfileCreateRequest request) {
        verifyUser(request.getUserId());
        Profile profile = profileMapper.toProfile(request);
        profileRepository.save(profile);
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    @PreAuthorize("hasAuthority('GET_ALL')")
    public List<ProfileResponse> getAllProfiles(String userId) {
        verifyUser(userId);
        return profileRepository.findAllByUserId(userId).stream().map(profileMapper::toProfileResponse).toList();
    }

    private void verifyUser(String userId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        String userIdInToken = jwt.getClaimAsString("user-id");
        if (!StringUtils.hasText(userIdInToken)) {
            throw new ApplicationException(ResponseMessage.UNAUTHORIZED);
        }

        if (!userId.equals(userIdInToken)) {
            throw new ApplicationException(ResponseMessage.FORBIDDEN);
        }

        UserResponse result = identityClient.getUser(userId).getData();
        if (!result.getId().equals(userId)) {
            throw new ApplicationException(ResponseMessage.USER_NOT_FOUND);
        }
        log.info(result.toString());
    }
}
