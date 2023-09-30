package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Order;
import com.stripe.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfoResponseDTO {

  private PriceDetailsResponseDTO priceDetails;
  private CardInfoResponseDTO cardInfo;

  public static PaymentInfoResponseDTO from(Order order, PaymentMethod paymentMethod) {
    return PaymentInfoResponseDTO.builder()
        .cardInfo(CardInfoResponseDTO.from(paymentMethod))
        .priceDetails(PriceDetailsResponseDTO.from(order))
        .build();
  }
}
