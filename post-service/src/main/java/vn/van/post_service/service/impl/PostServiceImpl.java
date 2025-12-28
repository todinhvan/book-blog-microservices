package vn.van.post_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.van.post_service.dto.request.PostCreateRequest;
import vn.van.post_service.dto.response.PostResponse;
import vn.van.post_service.entity.Post;
import vn.van.post_service.mapper.PostMapper;
import vn.van.post_service.repository.PostRepository;
import vn.van.post_service.service.PostService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;

    @Override
    public PostResponse createPost(PostCreateRequest request) {
        Post post = new Post();
        post.setContent(request.getContent());
        post.setUserId(extractUserId());
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @Override
    public List<PostResponse> getMyPosts() {
        return postRepository.findAllByUserId(extractUserId()).stream().map(postMapper::toPostResponse).toList();
    }

    private String extractUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("user-id");
    }
}
