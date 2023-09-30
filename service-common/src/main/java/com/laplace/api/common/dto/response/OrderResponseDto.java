package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.util.DateUtil;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {

  private String orderId;
  private String itemId;
  private String itemName;
  private String brandName;
  private String coverImage;
  private Long displayRequestDate;
  private Long purchasedDate;
  private Integer totalPrice;
  private String sellerEmail;
  private String buyerEmail;

  public static OrderResponseDto make(Order order, String sellerEmail, String buyerEmail) {
    return OrderResponseDto.builder()
        .orderId(order.getOrderId())
        .itemId(order.getItemId())
        .itemName(order.getItem().getItemName())
        .brandName(order.getItem().getBrandName())
        .coverImage(order.getItem().getCoverImage())
        .displayRequestDate(DateUtil.toEpochMilli(order.getItem().getDisplayRequestDate()))
        .purchasedDate(DateUtil.toEpochMilli(order.getCreatedAt()))
        .totalPrice(order.getOrderAmount())
        .sellerEmail(sellerEmail)
        .buyerEmail(buyerEmail)
        .build();
  }
}
