package com.laplace.api.common.dto.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.notification.NegotiationNotificationDTO;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.util.DateUtil;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificationDetailsDtoConverter implements
    Converter<Notification, NotificationDetailsResponseDto> {

  private final ItemService itemService;
  private final ObjectMapper objectMapper;

  @Autowired
  public NotificationDetailsDtoConverter(ItemService itemService,
      ObjectMapper objectMapper) {
    this.itemService = itemService;
    this.objectMapper = objectMapper;
  }

  @Override
  public NotificationDetailsResponseDto convert(Notification notification) {
    return NotificationDetailsResponseDto.builder()
        .id(notification.getId())
        .title(notification.getTitle())
        .dataOfMessage(fromJson(notification.getDataOfMessage()))
        .type(notification.getType())
        .date(DateUtil.toEpochMilli(notification.getCreatedOn()))
        .itemId(notification.getItemId())
        .readStatus(notification.isReadStatus())
        .build();
  }

  private <T> T fromJson(String json) {
    if (StringUtils.isBlank(json)) {
      return null;
    }

    try {
      return objectMapper.readValue(json, new TypeReference<>() {
      });
    } catch (Exception e) {
      return null;
    }
  }
}
