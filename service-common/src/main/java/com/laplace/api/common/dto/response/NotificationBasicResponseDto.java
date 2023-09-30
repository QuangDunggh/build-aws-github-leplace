package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.NotificationType;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationBasicResponseDto {
  private String id;
  private String title;
  private NotificationType type;
  private Long date;
  private Boolean readStatus;
}
