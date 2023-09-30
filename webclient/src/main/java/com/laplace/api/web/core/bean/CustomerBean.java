package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class CustomerBean {

  @Email(message = ErrorCode.INVALID_EMAIL_PATTERN)
  private String email;
  @Min(4)
  @Max(500)
  private String description;
  @Min(4)
  @Max(100)
  private String defaultCard;
  @Min(4)
  @Max(100)
  private String paymentMethod;
  @Min(4)
  @Max(500)
  private String metadata;
}
