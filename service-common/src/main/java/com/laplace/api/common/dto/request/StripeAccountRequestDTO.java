package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ErrorCode;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class StripeAccountRequestDTO {

  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  private String externalAccount;
  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  private String ip;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer addressId;
}
