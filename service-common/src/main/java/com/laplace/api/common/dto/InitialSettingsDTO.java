package com.laplace.api.common.dto;


import static com.laplace.api.common.constants.ApplicationConstants.HUNDRED;
import static com.laplace.api.common.constants.ApplicationConstants.MAX_MONTH;
import static com.laplace.api.common.constants.ApplicationConstants.MIN_MONTH;
import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.RemindPeriod;
import com.laplace.api.common.model.db.InitialSettings;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitialSettingsDTO {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Range(min = VALUE_ZERO, max = HUNDRED)
  private Integer processingRate;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer deliveryFee;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer deliveryFeeHokkaido;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer deliveryFeeOkinawa;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer deliveryFeeIsland;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer cancelFee;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer cancelDeliveryFee;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer cancelDeliveryFeeHokkaido;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer cancelDeliveryFeeOkinawa;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer cancelDeliveryFeeIsland;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer displayExtensionFee;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Range(min = MIN_MONTH, max = MAX_MONTH)
  private Integer displayPeriod;

  @JsonIgnore
  private RemindPeriod remindPeriod;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(VALUE_ZERO)
  private Integer giftWrappingFee;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private List<@Valid GiftWrappingDTO> giftWrappingOptions;

  @JsonIgnore
  private Integer updatedBy;

  public static InitialSettingsDTO from(InitialSettings initialSettings) {
    return Objects.isNull(initialSettings) ? makeMinimum() :
        InitialSettingsDTO.builder()
            .processingRate(initialSettings.getProcessingRate())
            .deliveryFee(initialSettings.getDeliveryFee())
            .deliveryFeeHokkaido(initialSettings.getDeliveryFeeHokkaido())
            .deliveryFeeOkinawa(initialSettings.getDeliveryFeeOkinawa())
            .deliveryFeeIsland(initialSettings.getDeliveryFeeIsland())
            .cancelFee(initialSettings.getCancelFee())
            .cancelDeliveryFee(initialSettings.getCancelDeliveryFee())
            .cancelDeliveryFeeHokkaido(initialSettings.getCancelDeliveryFeeHokkaido())
            .cancelDeliveryFeeOkinawa(initialSettings.getCancelDeliveryFeeOkinawa())
            .cancelDeliveryFeeIsland(initialSettings.getCancelDeliveryFeeIsland())
            .displayExtensionFee(initialSettings.getDisplayExtensionFee())
            .displayPeriod(initialSettings.getDisplayPeriod())
            .remindPeriod(initialSettings.getRemindPeriod())
            .giftWrappingFee(initialSettings.getGiftWrappingFee())
            .giftWrappingOptions(initialSettings.getGiftWrappingOptions().stream()
                .map(GiftWrappingDTO::from)
                .sorted(Comparator.comparing(GiftWrappingDTO::getId))
                .collect(Collectors.toList()))
            .build();
  }

  private static InitialSettingsDTO makeMinimum() {
    return InitialSettingsDTO.builder()
        .processingRate(VALUE_ZERO)
        .deliveryFee(VALUE_ZERO)
        .deliveryFeeHokkaido(VALUE_ZERO)
        .deliveryFeeOkinawa(VALUE_ZERO)
        .deliveryFeeIsland(VALUE_ZERO)
        .cancelFee(VALUE_ZERO)
        .cancelDeliveryFee(VALUE_ZERO)
        .displayExtensionFee(VALUE_ZERO)
        .displayPeriod(MIN_MONTH)
        .remindPeriod(RemindPeriod.ONE_WEEK)
        .giftWrappingFee(VALUE_ZERO)
        .build();
  }
}
