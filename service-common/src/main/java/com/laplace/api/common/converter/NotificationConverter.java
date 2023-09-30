package com.laplace.api.common.converter;

import com.laplace.api.common.configuration.notification.NotificationTemplateConfiguration;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter implements Converter<NotificationDto, Notification> {

  private final NotificationTemplateConfiguration templateConfiguration;

  @Autowired
  public NotificationConverter(NotificationTemplateConfiguration templateConfiguration) {
    this.templateConfiguration = templateConfiguration;
  }

  @Override
  public Notification convert(NotificationDto notificationDto) {
    return Notification.builder()
        .id(DateUtil.getUniqueTimeBasedUUID())
        .fromUserId(notificationDto.getFromUserId())
        .userId(notificationDto.getUserId())
        .title(templateConfiguration.getTitle(notificationDto.getType()))
        .dataOfMessage(notificationDto.getDataOfMessage())
        .itemId(notificationDto.getItemId())
        .createdBy(notificationDto.getFromUserId())
        .createdOn(DateUtil.timeNow())
        .type(notificationDto.getType())
        .build();
  }
}
