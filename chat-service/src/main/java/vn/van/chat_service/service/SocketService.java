package vn.van.chat_service.service;

import vn.van.chat_service.dto.response.IntrospectResponse;

public interface SocketService {
    IntrospectResponse introspect(String token);
}
