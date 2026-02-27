package vn.van.spring_ai_service.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.van.spring_ai_service.dto.request.ChatRequest;
import vn.van.spring_ai_service.dto.response.BillItem;
import vn.van.spring_ai_service.dto.response.ExpenseInfo;
import vn.van.spring_ai_service.dto.response.FilmInfo;
import vn.van.spring_ai_service.service.ChatService;

import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatServiceImpl implements ChatService {
    ChatClient chatClient;
    JdbcChatMemoryRepository jdbcChatMemoryRepository;

    public ChatServiceImpl(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30)
                .build();

        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public String chatMessage(ChatRequest request) {
        String conversationId = "conversation1";
        SystemMessage systemMessage = new SystemMessage("""
                You are VanAI
                You should response with a formal voice
                """);
        UserMessage userMessage = new UserMessage(request.message());
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return chatClient.prompt(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

    @Override
    public List<FilmInfo> chatFilm(ChatRequest request) {
        return chatClient.prompt(request.message())
                .system("You are VanAI")
                .call()
                .entity(new ParameterizedTypeReference<List<FilmInfo>>() {});
    }

    @Override
    public ExpenseInfo chatExpense(ChatRequest request) {
        return chatClient.prompt(request.message())
                .system("You are VanAI")
                .call()
                .entity(ExpenseInfo.class);
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
                .call()
                .content();
    }

    @Override
    public List<BillItem> chatWithBillImage(MultipartFile file, String message) {
        Media media = Media.builder()
                .data(file.getResource())
                .mimeType(MimeTypeUtils.parseMimeType(Objects.requireNonNull(file.getContentType())))
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .temperature(0.0) // Độ sáng tạo 0.0 -> 1.0
                .build();

        return chatClient.prompt()
                .options(chatOptions)
                .system("""
                        You are VanAI
                        The numbers are formatted in Vietnamese locale
                        """)
                .user(promptUserSpec -> promptUserSpec.media(media).text(message))
                .call()
                .entity(new ParameterizedTypeReference<List<BillItem>>() {});
    }
}
