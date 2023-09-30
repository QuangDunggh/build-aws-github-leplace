package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.validators.ValidPassword;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordRequest {

  @NotEmpty(message = ErrorCode.INVALID_PASSWORD)
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String oldPassword;
  @NotEmpty(message = ErrorCode.INVALID_PASSWORD)
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String newPassword;
  @NotEmpty(message = ErrorCode.INVALID_PASSWORD)
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String verifyPassword;
}
