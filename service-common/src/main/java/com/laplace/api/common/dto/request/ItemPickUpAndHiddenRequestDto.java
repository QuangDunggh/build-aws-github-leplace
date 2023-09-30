package com.laplace.api.common.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPickUpAndHiddenRequestDto {

  private String itemId;
  private Boolean isHidden;
  private Boolean isPicKup;
}
