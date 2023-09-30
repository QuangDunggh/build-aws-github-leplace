package com.laplace.api.common.dto.notification;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class FollowerNotificationDTO {

  private String userName;

  public static String makeJson(ObjectMapper objectMapper, String userName) {
    try {
      return objectMapper.writeValueAsString(FollowerNotificationDTO.builder()
          .userName(userName)
          .build());
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }
  }
}
