package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseResponseDTO {

  private Long purchaseTime;
  private String deliverySlipNumber;
  private MyClosetResponseDTO itemInfo;
  private PaymentInfoResponseDTO paymentInfo;
  private ShippingInfoResponseDTO shippingInfo;
  private AddressDto address;
}
