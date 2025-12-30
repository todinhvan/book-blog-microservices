package vn.van.chat_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.van.chat_service.entity.WebSocketSession;
import vn.van.chat_service.repository.WebSocketSessionRepository;
import vn.van.chat_service.service.WebSocketSessionService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketSessionServiceImpl implements WebSocketSessionService {
    WebSocketSessionRepository webSocketSessionRepository;

    @Override
    public WebSocketSession create(WebSocketSession webSocketSession) {
        return webSocketSessionRepository.save(webSocketSession);
    }

    @Override
    public void delete(String sessionId) {
        webSocketSessionRepository.deleteBySessionId(sessionId);
    }
}
