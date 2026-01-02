package vn.van.spring_ai_service.service;

import org.springframework.web.multipart.MultipartFile;
import vn.van.spring_ai_service.dto.request.ChatRequest;

public interface ChatService {
    String chat(ChatRequest request);
    String chatWithImage(MultipartFile file, String message);
}
