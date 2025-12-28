package vn.van.post_service.service;

import vn.van.post_service.dto.request.PostCreateRequest;
import vn.van.post_service.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostCreateRequest request);
    List<PostResponse> getMyPosts();
}
