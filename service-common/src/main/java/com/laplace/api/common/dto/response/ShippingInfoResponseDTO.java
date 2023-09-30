package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingInfoResponseDTO {

  private String shippingName;
  private String shippingKatakanaName;
  private String shippingEmail;
  private String shippingPhone;

  public static ShippingInfoResponseDTO from(Order order) {
    return ShippingInfoResponseDTO.builder()
        .shippingName(order.getShippingName())
        .shippingEmail(order.getShippingEmail())
        .shippingPhone(order.getShippingPhone())
        .shippingKatakanaName(order.getShippingKatakanaName())
        .build();
  }
}
