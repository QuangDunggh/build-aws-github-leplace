package com.laplace.api.common.converter.response;

import static com.laplace.api.common.constants.StatusConstants.DISPLAYABLE_STATUSES;

import com.laplace.api.common.dto.request.SellConditionRequestDto;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PaymentUtil;
import org.springframework.stereotype.Component;

@Component
public class SellConditionConverter {

  public Item makeItem(SellConditionRequestDto dto, Item item) {
    if (dto.getSellerComment() != null) {
      item.setSellerComment(dto.getSellerComment());
    }
    item.setDeliveryFeeBearer(dto.getDeliveryFeeBearer());
    if (DISPLAYABLE_STATUSES.contains(item.getStatus())) {
      item.setDiscountPercentage(
          PaymentUtil.calculateDiscountPrice(dto.getSellingPrice(), item.getDisplayPrice()));
    }
    item.setPrevDisplayPrice(item.getDisplayPrice());
    item.setDisplayPrice(dto.getSellingPrice());
    item.setLastUpdatedBy(item.getSellerId());
    item.setLastUpdatedOn(DateUtil.timeNow());
    return item;
  }
}
