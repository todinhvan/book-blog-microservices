package vn.van.spring_ai_service.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import vn.van.spring_ai_service.dto.request.ChatRequest;
import vn.van.spring_ai_service.service.ChatService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatServiceImpl implements ChatService {
    ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String chat(ChatRequest request) {
        return chatClient.prompt(request.getMessage()).call().content();
    }
}
