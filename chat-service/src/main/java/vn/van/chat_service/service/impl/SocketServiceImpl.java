package vn.van.chat_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.van.chat_service.dto.request.AuthenticationRequest;
import vn.van.chat_service.dto.response.IntrospectResponse;
import vn.van.chat_service.repository.http_client.IdentityClient;
import vn.van.chat_service.service.SocketService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketServiceImpl implements SocketService {
    IdentityClient identityClient;

    @Override
    public IntrospectResponse introspect(String token) {
        IntrospectResponse response = new IntrospectResponse();
        response.setValid(true);

        try {
            AuthenticationRequest request = new AuthenticationRequest();
            request.setToken(token);
            var introspectData = identityClient.introspect(request);
            if (Objects.isNull(introspectData)) {
                response.setValid(false);
            }
            response.setValid(introspectData.getData().isValid());
            response.setUserId(introspectData.getData().getUserId());
        } catch (Exception e) {
            response.setValid(false);
        }

        return response;
    }
}
