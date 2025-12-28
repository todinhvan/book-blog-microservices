package vn.van.post_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.van.post_service.constant.ResponseMessage;
import vn.van.post_service.dto.request.PostCreateRequest;
import vn.van.post_service.dto.response.ApiResponse;
import vn.van.post_service.dto.response.PostResponse;
import vn.van.post_service.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostController {
    PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostCreateRequest request) {
        return ResponseEntity.ok(toApiResponse(ResponseMessage.POST_CREATED, postService.createPost(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> getMyPosts() {
        return  ResponseEntity.ok(toApiResponse(ResponseMessage.GET_MY_POSTS, postService.getMyPosts()));
    }

    private <T> ApiResponse<T> toApiResponse(ResponseMessage responseMessage, T data) {
        return ApiResponse.<T>builder()
                .statusCode(responseMessage.getStatusCode())
                .status(responseMessage.getStatus())
                .message(responseMessage.getMessage())
                .data(data)
                .build();
    }
}
