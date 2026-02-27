package vn.van.chat_service.service;

import vn.van.chat_service.entity.WebSocketSession;

public interface WebSocketSessionService {
    WebSocketSession create(WebSocketSession webSocketSession);
    void delete(String sessionId);
}
