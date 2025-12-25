package vn.van.identity_service.config;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Instant issuedAt = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            Instant expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            Map<String, Object> headers = signedJWT.getHeader().toJSONObject();
            Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();

            return new Jwt(token, issuedAt, expiresAt, headers, claims);
        } catch (ParseException e) {
            throw new JwtException("Token invalid");
        }
    }
}
