package com.laplace.api.cms.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationCountResponseDto {

  private Integer advertiseCount;
  private Integer borrowCount;
  private Integer acceptRejectCount;
}