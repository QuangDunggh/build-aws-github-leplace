package com.laplace.api.common.converter;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.model.db.InitialSettings;
import com.laplace.api.common.util.DateUtil;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InitialSettingsConverter implements Converter<InitialSettingsDTO, InitialSettings> {

  private final GiftWrappingConverter giftWrappingConverter;

  public InitialSettingsConverter(
      GiftWrappingConverter giftWrappingConverter) {
    this.giftWrappingConverter = giftWrappingConverter;
  }

  @Override
  public InitialSettings convert(InitialSettingsDTO initialSettingsDTO) {
    return InitialSettings.builder()
        .id(ApplicationConstants.ONE)
        .processingRate(initialSettingsDTO.getProcessingRate())
        .deliveryFee(initialSettingsDTO.getDeliveryFee())
        .deliveryFeeHokkaido(initialSettingsDTO.getDeliveryFeeHokkaido())
        .deliveryFeeOkinawa(initialSettingsDTO.getDeliveryFeeOkinawa())
        .deliveryFeeIsland(initialSettingsDTO.getDeliveryFeeIsland())
        .cancelFee(initialSettingsDTO.getCancelFee())
        .cancelDeliveryFee(initialSettingsDTO.getCancelDeliveryFee())
        .cancelDeliveryFeeHokkaido(initialSettingsDTO.getCancelDeliveryFeeHokkaido())
        .cancelDeliveryFeeOkinawa(initialSettingsDTO.getCancelDeliveryFeeOkinawa())
        .cancelDeliveryFeeIsland(initialSettingsDTO.getCancelDeliveryFeeIsland())
        .displayExtensionFee(initialSettingsDTO.getDisplayExtensionFee())
        .displayPeriod(initialSettingsDTO.getDisplayPeriod())
        .remindPeriod(initialSettingsDTO.getRemindPeriod())
        .giftWrappingFee(initialSettingsDTO.getGiftWrappingFee())
        .giftWrappingOptions(initialSettingsDTO.getGiftWrappingOptions().stream()
            .map(giftWrappingConverter::convert)
            .collect(Collectors.toSet()))
        .lastUpdatedBy(initialSettingsDTO.getUpdatedBy())
        .lastUpdatedOn(DateUtil.timeNow())
        .build();
  }
}
