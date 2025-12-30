package vn.van.chat_service.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SocketController {
    SocketIOServer socketIOServer;

    @PostConstruct
    public void start() {
        socketIOServer.start();
        socketIOServer.addListeners(this);
        log.info("Socket Server started");
    }

    @OnConnect
    public void connect(SocketIOClient client) {
        log.info("Client connected: {}", client.getSessionId());
    }

    @OnDisconnect
    public void disconnect(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
    }

    @PreDestroy
    public void stop() {
        socketIOServer.stop();
        log.info("Socket Server stopped");
    }
}
