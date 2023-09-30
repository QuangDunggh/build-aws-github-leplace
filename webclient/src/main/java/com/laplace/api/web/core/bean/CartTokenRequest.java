package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartTokenRequest {

  @NotNull(message = ErrorCode.CLIENT_ID_NOT_FOUND)
  private Integer clientId;
  private Integer userId;
}
