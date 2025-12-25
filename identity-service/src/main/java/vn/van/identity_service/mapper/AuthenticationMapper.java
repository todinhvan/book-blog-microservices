package vn.van.identity_service.mapper;

import org.mapstruct.Mapper;
import vn.van.identity_service.dto.request.RegisterRequest;
import vn.van.identity_service.entity.User;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    User toUser(RegisterRequest request);
}
