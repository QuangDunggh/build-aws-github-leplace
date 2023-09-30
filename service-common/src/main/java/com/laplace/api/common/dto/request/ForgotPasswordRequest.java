package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {

  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX, message = ErrorCode.INVALID_EMAIL_PATTERN)
  @NotEmpty(message = ErrorCode.EMAIL_REQUIRED)
  private String email;
  private String lang;
}
