package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Order;
import com.stripe.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardInfoResponseDTO {

  private String cardType;
  private String lastFourDigit;

  public static CardInfoResponseDTO from(PaymentMethod paymentMethod) {
    return CardInfoResponseDTO.builder()
        .cardType(paymentMethod.getCard().getBrand())
        .lastFourDigit(paymentMethod.getCard().getLast4())
        .build();
  }
}
