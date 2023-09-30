package com.laplace.api.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

  private Integer goodsId;

  private Integer discountType;

  private Integer value;
}
