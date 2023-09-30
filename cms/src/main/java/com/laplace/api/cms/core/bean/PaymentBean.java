package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentBean {

  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  private String orderId;

  private Integer price;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer paymentType;

  @Size(min = 3, max = 100)
  private String chargeId;

  @Size(min = 3, max = 3)
  private String currency;

  @Size(min = 3, max = 500)
  private String description;

}
