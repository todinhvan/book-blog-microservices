package vn.van.chat_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import vn.van.chat_service.entity.Conversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Optional<Conversation> findByParticipantsHash(String hash);

    @Query("{'participants.userId' : ?0}")
    List<Conversation> findAllByParticipantUserId(String userId);
}
