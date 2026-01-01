package vn.van.identity_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public List<GrantedAuthority> convert(Jwt source) {
        List<GrantedAuthority> authorities = new LinkedList<>();
        // Authorities từ scope
        scopeConverter.setAuthorityPrefix("");
        Collection<GrantedAuthority> scopeAuthorities = scopeConverter.convert(source);
        if (Objects.nonNull(scopeAuthorities)) {
            authorities.addAll(scopeAuthorities);
        }

        // Authorities từ Keycloak
        Map<String, Object> realmAccessMap = source.getClaimAsMap("realm_access");
        if (Objects.nonNull(realmAccessMap)) {
            Object roles = realmAccessMap.get("roles");
            if (roles instanceof Collection<?> roleList) {
                roleList.stream()
                        .filter(String.class::isInstance)
                        .map(role -> new SimpleGrantedAuthority((String) role))
                        .forEach(authorities::add);
            }
        }

        return authorities;
    }
}
