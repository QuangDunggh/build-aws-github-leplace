package com.laplace.api.common.dto.response;

import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.util.DateUtil;
import com.stripe.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDetailsDto {

  private String orderId;
  private String itemId;
  private String itemName;
  private String brandName;
  private String coverImage;
  private String color;
  private String size;
  private Long displayRequestDate;
  private Long purchasedDate;
  private Integer itemPrice;
  private Integer processingFee;
  private Integer deliveryFee;
  private Integer giftWrappingFee;
  private Integer totalPrice;
  private String sellerEmail;
  private String buyerEmail;
  private String shippingName;
  private String shippingKatakanaName;
  private String shippingEmail;
  private String shippingPhone;
  private CardInfoResponseDTO cardInfo;
  private AddressDto addressInfo;
  private GiftWrappingDTO giftWrappingInfo;

  public static OrderResponseDetailsDto make(Order order, Payment payment,
      PaymentMethod paymentMethod, String sellerEmail, String buyerEmail, Address address,
      GiftWrappingDTO giftWrappingDTO) {
    return OrderResponseDetailsDto.builder()
        .orderId(order.getOrderId())
        .itemId(order.getItemId())
        .itemName(order.getItem().getItemName())
        .brandName(order.getItem().getBrandName())
        .coverImage(order.getItem().getCoverImage())
        .color(order.getItem().getColor())
        .size(order.getItem().getSize())
        .displayRequestDate(DateUtil.toEpochMilli(order.getItem().getDisplayRequestDate()))
        .purchasedDate(DateUtil.toEpochMilli(order.getCreatedAt()))
        .itemPrice(payment.getItemOriginalPrice())
        .processingFee(payment.getProcessingFee())
        .deliveryFee(payment.getDeliveryFee())
        .giftWrappingFee(payment.getGiftWrappingPrice())
        .totalPrice(order.getOrderAmount())
        .sellerEmail(sellerEmail)
        .buyerEmail(buyerEmail)
        .shippingName(order.getShippingName())
        .shippingKatakanaName(order.getShippingKatakanaName())
        .shippingEmail(order.getShippingEmail())
        .shippingPhone(order.getShippingPhone())
        .cardInfo(CardInfoResponseDTO.from(paymentMethod))
        .addressInfo(AddressDto.from(address))
        .giftWrappingInfo(giftWrappingDTO)
        .build();
  }
}
