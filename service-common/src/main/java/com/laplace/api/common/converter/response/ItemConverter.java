package com.laplace.api.common.converter.response;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.dto.request.ItemRequestDto;
import com.laplace.api.common.model.db.Brand;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.util.DateUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Component
public class ItemConverter {

  public Item makeItem(ItemRequestDto dto, Item info, Brand brand,
      Pair<String, String> subCategoryPair) {
    info.setColor(checkNull(dto.getColor(), info.getColor()));
    info.setCategory(checkNull(dto.getCategory(), info.getCategory()));
    info.setSubCategory(checkNull(buildSubCategoryString(subCategoryPair), info.getSubCategory()));
    info.setTargetAudience(checkNull(dto.getTargetAudience(), info.getTargetAudience()));
    info.setItemName(checkNull(dto.getName(), info.getItemName()));
    info.setDescriptions(checkNull(dto.getDescription(), info.getDescriptions()));
    info.setInspectionReport(checkNull(dto.getInspectionReport(), info.getInspectionReport()));
    info.setSize(checkNull(dto.getSize(), info.getSize()));
    info.setDimensions(checkNull(dto.getDimensions(), info.getDimensions()));
    info.setDeliveryFeeBearer(checkNull(dto.getDeliveryFeeBearer(), info.getDeliveryFeeBearer()));
    info.setPhysicalCondition(checkNull(dto.getPhysicalCondition(), info.getPhysicalCondition()));
    info.setSellerPurchaseLocation(
        checkNull(dto.getSellerPurchaseLocation(), info.getSellerPurchaseLocation()));
    info.setSellerPurchaseTime(
        checkNull(dto.getSellerPurchaseTime(), info.getSellerPurchaseTime()));
    info.setReceiptAvailable(checkNull(dto.isReceiptAvailable(), info.getReceiptAvailable()));
    info.setReceiptImage(checkNull(dto.getReceiptImage(), info.getReceiptImage()));
    info.setDisplayPrice(checkNull(dto.getDisplayPrice(), info.getDisplayPrice()));
    info.setLastUpdatedBy(checkNull(dto.getLastUpdatedBy(), info.getLastUpdatedBy()));
    info.setLastUpdatedOn(DateUtil.timeNow());
    info.buildBrandId(brand);
    info.setDesignerTime(checkNull(dto.getDesignerTime(), info.getDesignerTime()));
    info.setHidden(!ObjectUtils.isEmpty(info.getHidden()) && info.getHidden());
    info.setBlacklisted(false);
    info.setPickUp((!ObjectUtils.isEmpty(info.getPickUp())) && info.getPickUp());
    info.setPackageId(checkNull(dto.getPackageId(), info.getPackageId()));
    info.setAddressId(checkNull(dto.getAddressId(), info.getAddressId()));
    info.setTaxRate(
        ObjectUtils.isEmpty(info.getTaxRate()) ? ApplicationConstants.TAX_RATE : info.getTaxRate());
    return info;
  }

  private <T> T checkNull(T current, T old) {
    return Objects.isNull(current) ? old : current;
  }

  // @param  subCategoryPair contains english and japanese name of sub-category
  private String buildSubCategoryString(Pair<String, String> subCategoryPair) {
    return subCategoryPair.getLeft() + StringUtils.COLON + subCategoryPair.getRight();
  }
}
