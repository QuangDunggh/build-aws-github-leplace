package com.laplace.api.common.dto.response;


import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchasedItemResponseDTO {

  private String itemName;
  private String descriptions;
  private String itemId;
  private ItemStatus itemStatus;
  private String image;
  private String brandName;
  private Integer displayPrice;
  private Integer prevDisplayPrice;
  private Integer discountPercentage;
  private String deliverySlipNumber;
  private Long purchaseTime;
  private String orderId;

  public static PurchasedItemResponseDTO from(Order order) {
    Item item = order.getItem();
    return PurchasedItemResponseDTO.builder()
        .itemName(item.getItemName())
        .descriptions(item.getDescriptions())
        .itemId(item.getItemId())
        .image(item.getCoverImage())
        .itemStatus(item.getStatus())
        .displayPrice(item.getDisplayPrice())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .discountPercentage(item.getDiscountPercentage())
        .brandName(item.getBrandName())
        .deliverySlipNumber(item.getDeliverySlipNumber())
        .purchaseTime(DateUtil.toEpochMilli(order.getCreatedAt()))
        .orderId(order.getOrderId())
        .build();
  }

}
