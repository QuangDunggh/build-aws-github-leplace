package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.validators.groups.CanNotBeEmpty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PriceNegotiationRequestDto {
    @NotNull(message = ErrorCode.INVALID_ARGUMENT)
    @Min(value = ApplicationConstants.MINIMUM_PURCHASE_PRICE, message = ErrorCode.INVALID_ARGUMENT)
    private Integer negotiationPrice;
}
