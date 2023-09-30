package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequestDto {

  @NotEmpty(message = ErrorCode.EMAIL_REQUIRED)
  private String email;

  @NotEmpty(message = ErrorCode.INVALID_PASSWORD)
  private String password;
}
