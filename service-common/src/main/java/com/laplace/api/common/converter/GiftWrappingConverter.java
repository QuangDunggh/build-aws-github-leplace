package com.laplace.api.common.converter;

import static com.laplace.api.common.constants.ApplicationConstants.ONE;

import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.model.db.GiftWrappingOption;
import com.laplace.api.common.util.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GiftWrappingConverter implements Converter<GiftWrappingDTO, GiftWrappingOption> {

  @Override
  public GiftWrappingOption convert(GiftWrappingDTO giftWrappingDTO) {
    return GiftWrappingOption.builder()
        .id(giftWrappingDTO.getId())
        .settingsId(ONE)
        .giftWrappingType(giftWrappingDTO.getGiftWrappingType())
        .dimension(giftWrappingDTO.getDimension())
        .description(giftWrappingDTO.getDescription())
        .imageUrl(giftWrappingDTO.getImage())
        .lastUpdatedBy(giftWrappingDTO.getUpdatedBy())
        .lastUpdatedOn(DateUtil.timeNow())
        .build();
  }
}
