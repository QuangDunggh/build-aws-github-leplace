package com.laplace.api.cms.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Valid
@Data
public class AdminUserProfileDto {

  @JsonIgnore
  private Integer userId;

  private MultipartFile profileImage;
  @JsonIgnore
  private String profileImageUrl;

  private MultipartFile favIconImage;
  @JsonIgnore
  private String favIconImageUrl;

  @NotEmpty(message = ErrorCode.NAME_EMPTY)
  @Size(min = TextLength.COMPANY_NAME_MIN, max = TextLength.COMPANY_NAME_MAX)
  private String name;
}