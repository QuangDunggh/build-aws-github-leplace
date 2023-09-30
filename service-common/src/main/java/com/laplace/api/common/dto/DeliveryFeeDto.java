package com.laplace.api.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryFeeDto {

  private Integer deliveryFee;
  private Integer cancelDeliveryFee;

  public static DeliveryFeeDto from(Integer deliveryFee, Integer cancelDeliveryFee) {
    return DeliveryFeeDto.builder()
        .deliveryFee(deliveryFee)
        .cancelDeliveryFee(cancelDeliveryFee)
        .build();
  }
}
