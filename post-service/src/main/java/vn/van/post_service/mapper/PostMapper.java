package vn.van.post_service.mapper;

import org.mapstruct.Mapper;
import vn.van.post_service.dto.response.PostResponse;
import vn.van.post_service.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
