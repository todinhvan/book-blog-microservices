package vn.van.post_service.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.van.post_service.dto.request.PostCreateRequest;
import vn.van.post_service.dto.response.PageResponse;
import vn.van.post_service.dto.response.PostResponse;
import vn.van.post_service.dto.response.ProfileResponse;
import vn.van.post_service.entity.Post;
import vn.van.post_service.mapper.PostMapper;
import vn.van.post_service.repository.PostRepository;
import vn.van.post_service.repository.http_client.ProfileClient;
import vn.van.post_service.service.PostService;
import vn.van.post_service.util.DateTimeFormatter;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    ProfileClient profileClient;
    DateTimeFormatter formatter;

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
    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        String userId = extractUserId();


        ProfileResponse profile = profileClient.getProfile(userId).getData();
        String displayName = StringUtils.hasText(profile.getFirstName() + profile.getLastName())
                                ? profile.getFirstName() + profile.getLastName()
                                : "User";

        Page<Post> pagePost = postRepository.findAllByUserId(userId, pageable);
        List<PostResponse> posts = pagePost.getContent().stream()
                .map(post -> {
                    PostResponse postResponse = postMapper.toPostResponse(post);
                    postResponse.setDisplayName(displayName);
                    postResponse.setCreated(formatter.format(post.getCreatedAt()));
                    return postResponse;
                }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(pagePost.getTotalElements())
                .totalPages(pagePost.getTotalPages())
                .data(posts)
                .build();
    }

    private String extractUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaimAsString("user-id");
    }
}
