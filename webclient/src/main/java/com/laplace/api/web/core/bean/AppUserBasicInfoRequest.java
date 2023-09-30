package com.laplace.api.web.core.bean;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import static com.laplace.api.common.constants.ApplicationConstants.MOBILE_MAX_LENGTH;
import static com.laplace.api.common.constants.ApplicationConstants.MOBILE_MIN_LENGTH;

@Data
public class AppUserBasicInfoRequest {

  @NotEmpty(message = ErrorCode.FIRST_NAME_EMPTY)
  @Size(max = TextLength.FORTY, message = ErrorCode.FIRST_NAME_SIZE)
  private String firstName;

  @NotEmpty(message = ErrorCode.LAST_NAME_EMPTY)
  @Size(max = TextLength.FORTY, message = ErrorCode.LAST_NAME_SIZE)
  private String lastName;

  @NotEmpty(message = ErrorCode.FIRST_NAME_KANA_EMPTY)
  @Size(max = TextLength.FORTY, message = ErrorCode.FIRST_NAME_KANA_SIZE)
  private String firstNameKana;

  @NotEmpty(message = ErrorCode.LAST_NAME_KANA_EMPTY)
  @Size(max = TextLength.FORTY, message = ErrorCode.LAST_NAME_KANA_SIZE)
  private String lastNameKana;

  @NotEmpty(message = ErrorCode.USER_NAME_EMPTY)
  @Size(max = TextLength.FORTY, message = ErrorCode.LAST_NAME_KANA_SIZE)
  private String userName;

  @NotNull(message = ErrorCode.BIRTH_DATE_NULL)
  @Range(min = 1, max = 31, message = ErrorCode.BIRTH_DATE_NULL)
  private Integer birthDay;

  @NotNull(message = ErrorCode.BIRTH_DATE_NULL)
  @Range(min = 1, max = 12, message = ErrorCode.BIRTH_DATE_NULL)
  private Integer birthMonth;

  @NotNull(message = ErrorCode.BIRTH_DATE_NULL)
  @Range(min = 1900, max = 2100, message = ErrorCode.BIRTH_DATE_NULL)
  private Integer birthYear;

  @NotEmpty
  @Pattern(regexp = ApplicationConstants.VALID_PHONE_REGEX_JAPANESE, message = ErrorCode.INVALID_PHONE_NUMBER)
  @Size(min = MOBILE_MIN_LENGTH, max = MOBILE_MAX_LENGTH, message = ErrorCode.INVALID_PHONE_NUMBER)
  private String phoneNumber;
}
