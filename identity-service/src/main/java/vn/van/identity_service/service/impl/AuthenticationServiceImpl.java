package vn.van.identity_service.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.constant.RoleType;
import vn.van.identity_service.dto.request.AuthenticationRequest;
import vn.van.identity_service.dto.request.LoginRequest;
import vn.van.identity_service.dto.request.ProfileCreateRequest;
import vn.van.identity_service.dto.request.RegisterRequest;
import vn.van.identity_service.dto.response.AuthenticationResponse;
import vn.van.identity_service.dto.response.IntrospectResponse;
import vn.van.identity_service.dto.response.ProfileResponse;
import vn.van.identity_service.entity.BlacklistToken;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.mapper.AuthenticationMapper;
import vn.van.identity_service.mapper.ProfileMapper;
import vn.van.identity_service.repository.BlacklistTokenRepository;
import vn.van.identity_service.repository.RoleRepository;
import vn.van.identity_service.repository.UserRepository;
import vn.van.identity_service.repository.http_client.ProfileClient;
import vn.van.identity_service.service.AuthenticationService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    BlacklistTokenRepository blacklistTokenRepository;
    ProfileClient profileClient;
    PasswordEncoder passwordEncoder;
    AuthenticationMapper authenticationMapper;
    ProfileMapper profileMapper;

    @NonFinal
    @Value("${app.config.jwt-secret}")
    String jwtSecret;

    @NonFinal
    @Value("${app.config.token-expire-time}")
    long tokenExpireTime;

    @NonFinal
    @Value("${app.config.refresh-token-expire-time}")
    long refreshTokenExpireTime;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = authenticationMapper.toUser(request);
        user.setRoles(Set.of(roleRepository
                .findById(RoleType.USER.name())
                .orElseThrow(() -> new ApplicationException(ResponseMessage.ROLE_NOT_FOUND))));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ResponseMessage.USER_EXISTED);
        }

        ProfileCreateRequest profileCreateRequest = profileMapper.toProfileCreateRequest(user);
        profileCreateRequest.setUserId(user.getId());
        ProfileResponse profileResponse = profileClient.createDefaultProfile(profileCreateRequest).getData();
        log.info("User Id of Profile: {}", profileResponse.getUserId());

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(generateToken(user));
        return response;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ApplicationException(ResponseMessage.LOGIN_FAILURE));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApplicationException(ResponseMessage.LOGIN_FAILURE);
        }

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(generateToken(user));
        return response;
    }

    @Override
    public void logout(AuthenticationRequest request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            saveBlacklistToken(
                    signedJWT.getJWTClaimsSet().getJWTID(),
                    signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (ParseException | JOSEException | ApplicationException e) {
            log.error(e.toString());
            log.warn("Force Logout");
        }
    }

    @Override
    public IntrospectResponse introspect(AuthenticationRequest request) {
        boolean isValid = true;
        try {
            verifyToken(request.getToken(), false);
        } catch (ParseException | JOSEException | ApplicationException e) {
            log.error(e.toString());
            isValid = false;
        }

        IntrospectResponse response = new IntrospectResponse();
        response.setValid(isValid);
        return response;
    }

    @Override
    public AuthenticationResponse refreshToken(AuthenticationRequest request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            saveBlacklistToken(
                    signedJWT.getJWTClaimsSet().getJWTID(),
                    signedJWT.getJWTClaimsSet().getExpirationTime());

            User user = userRepository
                    .findByEmail(signedJWT.getJWTClaimsSet().getSubject())
                    .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(generateToken(user));
            return response;
        } catch (ParseException | JOSEException e) {
            log.error(e.toString());
            throw new ApplicationException(ResponseMessage.TOKEN_INVALID);
        }
    }

    private void saveBlacklistToken(String jwtId, Date expirationTime) {
        BlacklistToken blacklistToken = new BlacklistToken();
        blacklistToken.setId(jwtId);
        blacklistToken.setExpirationTime(expirationTime);
        blacklistTokenRepository.save(blacklistToken);
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());
        Date expirationTime = isRefresh
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(refreshTokenExpireTime, ChronoUnit.MINUTES)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();

        boolean isVerify = signedJWT.verify(verifier);
        boolean isNotExpire = expirationTime.after(new Date());
        boolean isNotExistedInBlacklist = !(blacklistTokenRepository.existsById(jwtId));

        if (!(isVerify && isNotExpire && isNotExistedInBlacklist)) {
            throw new ApplicationException(ResponseMessage.TOKEN_INVALID);
        }

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("van.vn")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(tokenExpireTime, ChronoUnit.MINUTES).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("user-id", user.getId())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(jwtSecret.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new ApplicationException(ResponseMessage.SECRET_KEY_INVALID);
        }
    }

    private String buildScope(User user) {
        StringJoiner sj = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                sj.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> sj.add(permission.getName()));
                }
            });
        }
        return sj.toString();
    }
}
