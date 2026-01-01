package vn.van.spring_ai_service.service;

import vn.van.spring_ai_service.dto.request.ChatRequest;

public interface ChatService {
    String chat(ChatRequest request);
}
