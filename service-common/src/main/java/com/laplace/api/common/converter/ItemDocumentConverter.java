package com.laplace.api.common.converter;

import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ONE;
import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;

import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.elasticsearch.CategoryDocument;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import com.laplace.api.common.util.DateUtil;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class ItemDocumentConverter implements Converter<Item, ItemDocument> {

  @Override
  public ItemDocument convert(@NonNull Item item) {
    ItemDocument document = ItemDocument.builder()
        .id(item.getItemId())
        .sellerId(item.getSellerId())
        .itemName(item.getItemName())
        .descriptions(item.getDescriptions())
        .category(item.getCategory().name())
        .coverImage(item.getCoverImage())
        .receiptAvailable(item.getReceiptAvailable())
        .receiptImage(item.getReceiptImage())
        .displayRequestDate(DateUtil.toEpochMilli(item.getDisplayRequestDate()))
        .publishedAt(DateUtil.toEpochMilli(item.getPublishedAt()))
        .color(item.getColor())
        .size(item.getSize())
        .dimensions(item.getDimensions())
        .displayPrice(item.getDisplayPrice())
        .discountPercentage(item.getDiscountPercentage())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .pickUp(item.getPickUp())
        .pickUpAt(DateUtil.toEpochMilli(item.getPickUpAt()))
        .hidden(item.getHidden())
        .blacklisted(item.getBlacklisted())
        .favoriteCount(item.getFavoriteCount())
        .createdBy(item.getCreatedBy())
        .createdOn(DateUtil.toEpochMilli(item.getCreatedOn()))
        .lastUpdatedBy(item.getLastUpdatedBy())
        .lastUpdatedOn(DateUtil.toEpochMilli(item.getLastUpdatedOn()))
        .onHoldAt(DateUtil.toEpochMilli(item.getOnHoldAt()))
        .estimatedPickUpTimeByLaplace(item.getEstimatedPickUpTimeByLaplace())
        .build();
    if (!ObjectUtils.isEmpty(item.getBrandId())) {
      String[] parts = item.getBrandId().split(StringUtils.COLON);
      document.setBrandId(Integer.parseInt(parts[VALUE_ONE]));
      document.setBrandName(parts[VALUE_ZERO]);
    }
    if (!ObjectUtils.isEmpty(item.getSellerPurchaseTime())) {
      document.setSellerPurchaseTime(item.getSellerPurchaseTime().name());
    }
    if (!ObjectUtils.isEmpty(item.getSellerEmail())) {
      document.setSellerEmail(item.getSellerEmail());
    }
    if (!ObjectUtils.isEmpty(item.getDeliveryFeeBearer())) {
      document.setDeliveryFeeBearer(item.getDeliveryFeeBearer().name());
    }
    if (!ObjectUtils.isEmpty(item.getPhysicalCondition())) {
      document.setPhysicalCondition(item.getPhysicalCondition().name());
    }
    if (!ObjectUtils.isEmpty(item.getSellerPurchaseLocation())) {
      document.setSellerPurchaseLocation(item.getSellerPurchaseLocation().name());
    }
    if (!ObjectUtils.isEmpty(item.getJudgementStatus())) {
      document.setJudgementStatus(item.getJudgementStatus().name());
    }
    if (!ObjectUtils.isEmpty(item.getStatus())) {
      document.setStatus(item.getStatus().name());
    }
    if (!ObjectUtils.isEmpty(item.getTargetAudience())) {
      document.setTargetAudience(item.getTargetAudience().name());
    }
    if (org.springframework.util.StringUtils.hasText(item.getSubCategory())) {
      String[] parts = item.getSubCategory().split(StringUtils.COLON);
      if (parts.length > VALUE_ONE) {
        document.setSubCategory(new CategoryDocument(parts[VALUE_ZERO], parts[VALUE_ONE]));
      } else {
        document.setSubCategory(new CategoryDocument(parts[VALUE_ZERO], StringUtils.EMPTY_STRING));
      }
    }

    return document;
  }
}
