package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyClosetResponseDTO {

  private String itemName;
  private String descriptions;
  private String itemId;
  private ItemStatus itemStatus;
  private String sellerEmail;
  private Long displayRequestDate;
  private Long listingRequestDate;
  private String image;
  private String brandName;
  private String dimensions;
  private Integer displayPrice;
  private Integer prevDisplayPrice;
  private Integer discountPercentage;
  private String color;
  private String size;
  private DeliveryFeeBearer deliveryFeeBearer;

  public static MyClosetResponseDTO from(Item item, String sellerEmail) {
    return MyClosetResponseDTO.builder()
        .itemName(item.getItemName())
        .descriptions(item.getDescriptions())
        .itemId(item.getItemId())
        .image(item.getCoverImage())
        .itemStatus(item.getStatus())
        .displayPrice(item.getDisplayPrice())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .displayRequestDate(DateUtil.toEpochMilli(item.getDisplayRequestDate()))
        .listingRequestDate(DateUtil.toEpochMilli(item.getCreatedOn()))
        .sellerEmail(sellerEmail)
        .brandName(item.getBrandName())
        .size(item.getSize())
        .color(item.getColor())
        .discountPercentage(item.getDiscountPercentage())
        .deliveryFeeBearer(item.getDeliveryFeeBearer())
        .build();
  }
}
