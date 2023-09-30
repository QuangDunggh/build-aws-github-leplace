package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemStatusChangeRequestDto {

  private String itemId;
  private ItemStatus status;
  private JudgementStatus judgementStatus;
  private String packageId;
  private Long expectedDateTime;
}
