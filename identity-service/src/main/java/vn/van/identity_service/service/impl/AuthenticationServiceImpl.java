package vn.van.identity_service.service.impl;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.AuthenticationRequest;
import vn.van.identity_service.dto.request.LoginRequest;
import vn.van.identity_service.dto.response.AuthenticationResponse;
import vn.van.identity_service.dto.response.IntrospectResponse;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.repository.UserRepository;
import vn.van.identity_service.service.AuthenticationService;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

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
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
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
            if (Objects.isNull(signedJWT)) {
                throw new ApplicationException(ResponseMessage.TOKEN_INVALID);
            }
        } catch (ParseException | JOSEException | ApplicationException e) {
            log.warn("Force Logout");
        }
    }

    @Override
    public IntrospectResponse introspect(AuthenticationRequest request) {
        boolean isValid = true;
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), false);
            if (Objects.isNull(signedJWT)) isValid = false;
        } catch (ParseException | JOSEException e) {
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
            if (Objects.isNull(signedJWT)) {
                throw new ApplicationException(ResponseMessage.TOKEN_INVALID);
            }

            User user = userRepository.findByEmail(signedJWT.getJWTClaimsSet().getSubject())
                    .orElseThrow(() -> new ApplicationException(ResponseMessage.USER_NOT_FOUND));
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(generateToken(user));
            return response;
        } catch (ParseException | JOSEException e) {
            throw new ApplicationException(ResponseMessage.TOKEN_INVALID);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());
        Date expirationTime = isRefresh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                    .toInstant().plus(refreshTokenExpireTime, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean isVerify = signedJWT.verify(verifier);
        boolean isNotExpire = expirationTime.after(new Date());

        return (isVerify && isNotExpire) ? signedJWT : null;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("van.vn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(tokenExpireTime, ChronoUnit.SECONDS).toEpochMilli()))
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
                    role.getPermissions().forEach(permission -> {
                        sj.add(permission.getName());
                    });
                }
            });
        }
        return sj.toString();
    }
}
