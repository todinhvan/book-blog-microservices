package vn.van.chat_service.service;

import vn.van.chat_service.dto.request.ConversationCreateRequest;
import vn.van.chat_service.dto.response.ConversationResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationResponse> myConversations();
    ConversationResponse create(ConversationCreateRequest request);
}
