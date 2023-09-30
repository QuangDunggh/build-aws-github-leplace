package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressUpdateRequest {

  private Integer id;

  @NotEmpty(message = ErrorCode.NAME_EMPTY)
  @Size(max = TextLength.COMPANY_NAME_MAX, message = ErrorCode.NAME_SIZE)
  private String name;

  @NotEmpty(message = ErrorCode.READING_EMPTY)
  @Size(max = TextLength.COMPANY_NAME_MAX, message = ErrorCode.READING_SIZE)
  private String reading;

  @NotEmpty(message = ErrorCode.ZIP_CODE_EMPTY)
  @Size(max = TextLength.EIGHT, message = ErrorCode.ZIP_CODE_SIZE)
  private String zipCode;

  @NotNull
  private Integer state;

  @NotEmpty(message = ErrorCode.CITY_EMPTY)
  @Size(max = TextLength.COMPANY_NAME_MAX, message = ErrorCode.CITY_SIZE)
  private String city;

  @NotEmpty(message = ErrorCode.ADDRESS_EMPTY)
  private String line1;

  private String line2;

  @NotEmpty
  @Pattern(regexp = ApplicationConstants.VALID_PHONE_REGEX, message = ErrorCode.INVALID_PHONE_NUMBER)
  private String phone;
}