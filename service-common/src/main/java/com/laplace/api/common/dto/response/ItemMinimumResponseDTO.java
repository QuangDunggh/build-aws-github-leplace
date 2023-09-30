package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemMinimumResponseDTO {

  private String itemName;
  private String itemId;
}
