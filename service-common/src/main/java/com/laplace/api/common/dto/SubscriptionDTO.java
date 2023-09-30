package com.laplace.api.common.dto;

import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String subscriptionName;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer subscriptionFee;
}
