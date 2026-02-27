package vn.van.identity_service.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.van.identity_service.constant.ResponseMessage;
import vn.van.identity_service.dto.response.KeycloakErrorResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class ErrorNormalizer {
    private final ObjectMapper objectMapper;
    private final Map<String, ResponseMessage> responseMessageMap;

    public ErrorNormalizer() {
        this.objectMapper = new ObjectMapper();
        this.responseMessageMap = new HashMap<>();

        this.responseMessageMap.put("User exists with same email", ResponseMessage.EMAIL_EXISTED);
        this.responseMessageMap.put("User exists with same username", ResponseMessage.USERNAME_EXISTED);
        this.responseMessageMap.put("User name is missing", ResponseMessage.USERNAME_MISSING);
    }

    public ApplicationException toApplicationException(FeignException exception) {
        try {
            log.warn("Cannot complete request: ", exception);
            var response = objectMapper.readValue(exception.contentUTF8(), KeycloakErrorResponse.class);
            if (Objects.nonNull(response.getErrorMessage()) && Objects.nonNull(responseMessageMap.get(response.getErrorMessage()))) {
                return new ApplicationException(responseMessageMap.get(response.getErrorMessage()));
            }

        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize content: ", e);
        }

        return new ApplicationException(ResponseMessage.UNCATEGORIZED);
    }
}
