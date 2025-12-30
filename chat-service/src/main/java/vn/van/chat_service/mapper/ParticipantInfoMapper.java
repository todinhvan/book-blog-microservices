package vn.van.chat_service.mapper;

import org.mapstruct.Mapper;
import vn.van.chat_service.dto.response.ProfileResponse;
import vn.van.chat_service.entity.ParticipantInfo;

@Mapper(componentModel = "spring")
public interface ParticipantInfoMapper {
    ParticipantInfo toParticipantInfo(ProfileResponse response);
}
