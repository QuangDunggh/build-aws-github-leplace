package com.laplace.api.common.dto.business;

import com.laplace.api.common.constants.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {

  private Integer fromUserId;
  private Integer userId;
  private NotificationType type;
  private String dataOfMessage;
  private String itemId;
}
