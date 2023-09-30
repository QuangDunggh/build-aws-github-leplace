package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.validators.ValidPassword;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignUpRequest {

  @NotEmpty(message = ErrorCode.INVALID_TOKEN)
  private String token;
  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX, message = ErrorCode.INVALID_EMAIL_PATTERN)
  private String email;
  @ValidPassword(message = ErrorCode.INVALID_PASSWORD_PATTERN)
  private String password;
}
