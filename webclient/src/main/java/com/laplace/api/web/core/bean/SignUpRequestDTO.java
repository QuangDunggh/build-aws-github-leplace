package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.AuthenticationType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpRequestDTO {

  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX, message = ErrorCode.INVALID_EMAIL_PATTERN)
  private String email;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private AuthenticationType type;
  private String password;
  private String socialId;
  private String snsAccessToken;
  private String twitterTokenSecret;
  private String lang;

}
