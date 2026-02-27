package vn.van.chat_service.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.van.chat_service.constant.ResponseMessage;
import vn.van.chat_service.dto.request.ConversationCreateRequest;
import vn.van.chat_service.dto.response.ApiResponse;
import vn.van.chat_service.dto.response.ConversationResponse;
import vn.van.chat_service.service.ConversationService;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConversationController {
    ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(@RequestBody @Valid ConversationCreateRequest request) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.CONVERSATION_CREATED, conversationService.create(request)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getMyConversations() {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.CONVERSATION_GET_MY, conversationService.myConversations()));
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
