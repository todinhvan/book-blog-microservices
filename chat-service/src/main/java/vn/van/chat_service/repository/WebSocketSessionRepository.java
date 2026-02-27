package vn.van.chat_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.van.chat_service.entity.WebSocketSession;

import java.util.List;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {
    void deleteBySessionId(String sessionId);
    List<WebSocketSession> findAllByUserIdIn(List<String> userIds);
}
