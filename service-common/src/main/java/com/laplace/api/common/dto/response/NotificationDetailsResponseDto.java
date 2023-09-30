package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDetailsResponseDto {

  private String id;
  private String title;
  private Object dataOfMessage;
  private NotificationType type;
  private Long date;
  private String itemId;
  private Boolean readStatus;
}
