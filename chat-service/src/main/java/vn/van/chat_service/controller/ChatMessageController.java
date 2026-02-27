package vn.van.chat_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.van.chat_service.constant.ResponseMessage;
import vn.van.chat_service.dto.request.ChatMessageCreateRequest;
import vn.van.chat_service.dto.response.ApiResponse;
import vn.van.chat_service.dto.response.ChatMessageResponse;
import vn.van.chat_service.service.ChatMessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatMessageController {
    ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatMessageResponse>> createMessage(@RequestBody @Valid ChatMessageCreateRequest request) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.MESSAGE_CREATED, chatMessageService.createMessage(request)));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getMessages(@PathVariable String conversationId) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.MESSAGE_GET, chatMessageService.getMessages(conversationId)));
    }

    private <T>ApiResponse<T> toApiResponse(ResponseMessage responseMessage, T data) {
        return ApiResponse.<T>builder()
                .statusCode(responseMessage.getStatusCode())
                .status(responseMessage.getStatus())
                .message(responseMessage.getMessage())
                .data(data)
                .build();
    }
}
