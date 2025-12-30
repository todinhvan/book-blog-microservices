package vn.van.chat_service.service;

import vn.van.chat_service.dto.request.ChatMessageCreateRequest;
import vn.van.chat_service.dto.response.ChatMessageResponse;

import java.util.List;

public interface ChatMessageService {
    ChatMessageResponse createMessage(ChatMessageCreateRequest request);
    List<ChatMessageResponse> getMessages(String conversationId);
}
