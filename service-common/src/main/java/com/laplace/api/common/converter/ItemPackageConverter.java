package com.laplace.api.common.converter;

import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.model.db.ItemPackage;
import com.laplace.api.common.util.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemPackageConverter implements Converter<ItemPackageRequestDTO, ItemPackage> {

  @Override
  public ItemPackage convert(ItemPackageRequestDTO itemPackageRequestDTO) {
    return ItemPackage.builder()
        .packageId(itemPackageRequestDTO.getPackageId())
        .userId(itemPackageRequestDTO.getUserId())
        .addressId(itemPackageRequestDTO.getAddressId())
        .estimatedShippingDate(itemPackageRequestDTO.getEstimatedShippingDate())
        .createdBy(itemPackageRequestDTO.getUserId())
        .createdOn(DateUtil.timeNow())
        .lastUpdatedBy(itemPackageRequestDTO.getUserId())
        .lastUpdatedOn(DateUtil.timeNow())
        .build();
  }
}
