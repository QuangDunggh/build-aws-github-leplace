package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryBean {

  @NotEmpty
  private String name;

  @NotEmpty
  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX, message = ErrorCode.INVALID_EMAIL_PATTERN)
  private String email;

  @NotNull
  private Integer type;

  @NotEmpty(message = ErrorCode.CONTENT_EMPTY)
  @Size(max = TextLength.TWO_THOUSAND, message = ErrorCode.CONTENT_SIZE)
  private String content;
}
