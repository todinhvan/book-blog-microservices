package vn.van.chat_service.mapper;

import org.mapstruct.Mapper;
import vn.van.chat_service.dto.response.ConversationResponse;
import vn.van.chat_service.entity.Conversation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse(Conversation conversation);
    List<ConversationResponse> toConversationResponseList(List<Conversation> conversations);
}