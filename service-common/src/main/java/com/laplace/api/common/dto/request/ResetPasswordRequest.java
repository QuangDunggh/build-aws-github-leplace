package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.validators.ValidPassword;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

  @NotEmpty(message = ErrorCode.INVALID_TOKEN)
  private String otp;
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String newPassword;
}
