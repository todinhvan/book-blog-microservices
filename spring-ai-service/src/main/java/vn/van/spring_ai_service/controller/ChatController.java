package vn.van.spring_ai_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.van.spring_ai_service.dto.request.ChatRequest;
import vn.van.spring_ai_service.service.ChatService;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;

    @PostMapping
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }

    @PostMapping("/image")
    public String image(
            @RequestParam MultipartFile file,
            @RequestParam String message
    ) {
        return chatService.chatWithImage(file, message);
    }
}
