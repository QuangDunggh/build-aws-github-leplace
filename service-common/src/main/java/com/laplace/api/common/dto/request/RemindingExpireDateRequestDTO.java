package com.laplace.api.common.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemindingExpireDateRequestDTO {

  @NotNull
  private String orderId;
}
