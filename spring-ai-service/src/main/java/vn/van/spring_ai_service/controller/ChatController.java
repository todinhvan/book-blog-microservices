package vn.van.spring_ai_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.van.spring_ai_service.dto.request.ChatRequest;
import vn.van.spring_ai_service.dto.response.BillItem;
import vn.van.spring_ai_service.dto.response.ExpenseInfo;
import vn.van.spring_ai_service.dto.response.FilmInfo;
import vn.van.spring_ai_service.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;

    @PostMapping
    public String chat(@RequestBody ChatRequest request) {
        return chatService.chatMessage(request);
    }

    @PostMapping("/film")
    public List<FilmInfo> chatFilm(@RequestBody ChatRequest request) {
        return chatService.chatFilm(request);
    }

    @PostMapping("/expense")
    public ExpenseInfo chatExpense(@RequestBody ChatRequest request) {
        return chatService.chatExpense(request);
    }

    @PostMapping("/image")
    public String image(
            @RequestParam MultipartFile file,
            @RequestParam String message
    ) {
        return chatService.chatWithImage(file, message);
    }

    @PostMapping("/image/bill")
    public List<BillItem> billImage(
            @RequestParam MultipartFile file,
            @RequestParam String message
    ) {
        return chatService.chatWithBillImage(file, message);
    }
}
