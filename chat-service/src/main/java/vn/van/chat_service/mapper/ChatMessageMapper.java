package vn.van.chat_service.mapper;

import org.mapstruct.Mapper;
import vn.van.chat_service.dto.request.ChatMessageCreateRequest;
import vn.van.chat_service.dto.response.ChatMessageResponse;
import vn.van.chat_service.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessage toChatMessage(ChatMessageCreateRequest request);
    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);
}
