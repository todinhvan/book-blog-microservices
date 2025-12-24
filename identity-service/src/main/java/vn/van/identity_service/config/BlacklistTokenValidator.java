package vn.van.identity_service.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.repository.BlacklistTokenRepository;

@Component
@RequiredArgsConstructor
public class BlacklistTokenValidator implements OAuth2TokenValidator<Jwt> {
    private final BlacklistTokenRepository blacklistTokenRepository;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (blacklistTokenRepository.existsById(token.getId())) {
            ResponseMessage responseMessage = ResponseMessage.UNAUTHORIZED;
            OAuth2Error error = new OAuth2Error(
                    String.valueOf(responseMessage.getStatusCode()), responseMessage.getMessage(), null);
            return OAuth2TokenValidatorResult.failure(error);
        }
        return OAuth2TokenValidatorResult.success();
    }
}
