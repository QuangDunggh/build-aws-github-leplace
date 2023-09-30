package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ActivationRequestDTO {

  @NotEmpty(message = ErrorCode.INVALID_TOKEN)
  @Size(max = TextLength.SIX, message = ErrorCode.INVALID_TOKEN)
  private String token;
}
