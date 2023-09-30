package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SellConditionRequestDto {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(value = ApplicationConstants.MINIMUM_PURCHASE_PRICE, message = ErrorCode.INVALID_ARGUMENT)
  private int sellingPrice;

  @NotNull
  private DeliveryFeeBearer deliveryFeeBearer;

  @Size(max = 256, message = ErrorCode.INVALID_ARGUMENT)
  private String sellerComment;
}
