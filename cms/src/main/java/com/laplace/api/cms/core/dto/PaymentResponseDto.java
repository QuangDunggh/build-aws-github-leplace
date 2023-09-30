package com.laplace.api.cms.core.dto;

import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDto {

  private Integer paymentId;
  private Long paymentDate;
  private String itemId;
  private String itemName;
  private String brandName;
  private String userEmail;
  private Integer itemPrice;
  private Integer processingFee;
  private Integer deliveryFee;
  private Integer giftWrappingFee;
  private Integer totalPrice;

  public static PaymentResponseDto from(Payment payment, Item item) {
    return PaymentResponseDto.builder()
        .paymentId(payment.getId())
        .paymentDate(DateUtil.toEpochMilli(payment.getCreatedAt()))
        .itemId(item.getItemId())
        .itemName(item.getItemName())
        .brandName(item.getBrandName())
        .userEmail(payment.getUserName())
        .itemPrice(payment.getItemOriginalPrice())
        .processingFee(payment.getProcessingFee())
        .deliveryFee(payment.getDeliveryFee())
        .giftWrappingFee(payment.getGiftWrappingPrice())
        .totalPrice(payment.getOriginalAmount())
        .build();
  }
}