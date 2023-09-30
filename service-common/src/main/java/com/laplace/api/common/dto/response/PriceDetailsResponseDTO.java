package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceDetailsResponseDTO {
    private Integer purchasePrice;
    private Integer shippingCharge;
    private Integer optionPrice;
    private Integer totalPrice;

  public static PriceDetailsResponseDTO from(Order order) {
    Optional<Payment> paymentOptional = order.getPayments().stream().filter(pay -> pay.getPaymentRefId().equals(
        ApplicationConstants.PAYMENT_PARENT_REF_ID)).findAny();
    if (paymentOptional.isEmpty()) {
      return emptyPriceDetailsResponseDTO();
    }
    Payment payment = paymentOptional.get();
    return PriceDetailsResponseDTO.builder()
        .purchasePrice(payment.getItemOriginalPrice())
        .shippingCharge(payment.getDeliveryFee())
        .optionPrice(payment.getGiftWrappingPrice())
        .totalPrice(payment.getOriginalAmount())
        .build();
  }

  private static PriceDetailsResponseDTO emptyPriceDetailsResponseDTO() {
    return  PriceDetailsResponseDTO.builder()
        .purchasePrice(0)
        .optionPrice(0)
        .shippingCharge(0)
        .totalPrice(0)
        .build();
  }
}
