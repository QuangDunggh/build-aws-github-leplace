package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.response.NotificationBasicResponseDto;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.util.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificationBasicDtoConverter implements
    Converter<Notification, NotificationBasicResponseDto> {

  @Override
  public NotificationBasicResponseDto convert(Notification notification) {
    return NotificationBasicResponseDto.builder()
        .id(notification.getId())
        .title(notification.getTitle())
        .type(notification.getType())
        .date(DateUtil.toEpochMilli(notification.getCreatedOn()))
        .readStatus(notification.isReadStatus())
        .build();
  }
}
