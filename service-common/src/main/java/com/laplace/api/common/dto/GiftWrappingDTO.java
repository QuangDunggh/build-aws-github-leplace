package com.laplace.api.common.dto;

import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.model.db.GiftWrappingOption;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftWrappingDTO {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer id;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private GiftWrappingType giftWrappingType;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String dimension;

  private String description;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String image;

  @JsonIgnore
  private Integer updatedBy;

  public static GiftWrappingDTO from(GiftWrappingOption giftWrappingOption) {
    return Objects.isNull(giftWrappingOption) ? makeMinimum() :GiftWrappingDTO.builder()
        .id(giftWrappingOption.getId())
        .giftWrappingType(giftWrappingOption.getGiftWrappingType())
        .dimension(giftWrappingOption.getDimension())
        .description(giftWrappingOption.getDescription())
        .image(giftWrappingOption.getImageUrl())
        .build();
  }

  private static GiftWrappingDTO makeMinimum() {
    return GiftWrappingDTO.builder()
        .id(VALUE_ZERO)
        .giftWrappingType(GiftWrappingType.NO_WRAPPING)
        .dimension(StringUtils.EMPTY_STRING)
        .description(StringUtils.EMPTY_STRING)
        .image(StringUtils.EMPTY_STRING)
        .build();
  }
}
