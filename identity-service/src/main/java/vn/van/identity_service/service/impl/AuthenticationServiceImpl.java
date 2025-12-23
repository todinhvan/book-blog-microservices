package vn.van.identity_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.request.LoginRequest;
import vn.van.identity_service.dto.response.LoginResponse;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.exception.ApplicationException;
import vn.van.identity_service.repository.UserRepository;
import vn.van.identity_service.service.AuthenticationService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${app.config.jwt-secret}")
    String jwtSecret;

    @NonFinal
    @Value("${app.config.token-expire-time}")
    long tokenExpireTime;

    @Override
    public LoginResponse login(LoginRequest request) throws JOSEException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApplicationException(ResponseMessage.LOGIN_FAILURE));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApplicationException(ResponseMessage.LOGIN_FAILURE);
        }

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("van.vn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(tokenExpireTime, ChronoUnit.SECONDS).toEpochMilli()))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        JWSSigner signer = new MACSigner(jwtSecret.getBytes());
        jwsObject.sign(signer);
        String token = jwsObject.serialize();

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
}
