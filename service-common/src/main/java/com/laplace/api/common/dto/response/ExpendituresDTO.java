package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.OrderStatus;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpendituresDTO {

  private String itemName;
  private String itemId;
  private String brandName;
  private Long paymentTime;
  private String paymentMethod;
  private String cardType;
  private String lastFourDigit;
  private OrderStatus feeType;
  private Integer totalPrice;

  public static ExpendituresDTO from(Order order, Item item) {
    return ExpendituresDTO.builder()
        .brandName(item.getBrandName())
        .itemId(item.getItemId())
        .itemName(item.getItemName())
        .paymentTime(DateUtil.toEpochMilli(order.getCreatedAt()))
        .totalPrice(order.getOrderAmount())
        .feeType(order.getStatus())
        .cardType("VISA")
        .paymentMethod("CREDIT")
        .lastFourDigit("1212")
        .build();
  }
}
