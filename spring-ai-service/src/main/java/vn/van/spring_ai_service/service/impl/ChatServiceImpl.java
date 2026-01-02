package vn.van.spring_ai_service.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.van.spring_ai_service.dto.request.ChatRequest;
import vn.van.spring_ai_service.service.ChatService;

import java.util.Objects;

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
        SystemMessage systemMessage = new SystemMessage("""
                You are VanAI
                You should response with a formal voice
                """);
        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .call().content();
    }

    @Override
    public String chatWithImage(MultipartFile file, String message) {
        Media media = Media.builder()
                .data(file.getResource())
                .mimeType(MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())))
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0.5) // Độ sáng tạo 0.0 -> 1.0
                .build();

        return chatClient.prompt()
                .options(chatOptions)
                .system("""
                        You are VanAI
                        You should response with a formal voice
                        """)
                .user(promptUserSpec -> promptUserSpec.media(media).text(message))
                .call().content();
    }
}
