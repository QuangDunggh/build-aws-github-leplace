package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.validators.ValidPassword;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordUpdateRequest {

  @NotEmpty
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String currentPass;

  @NotEmpty
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String newPass;
}
