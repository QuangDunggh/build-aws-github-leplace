package com.laplace.api.common.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRequestDTO {

  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  @Size(min = TextLength.BRAND_NAME_MIN, max = TextLength.BRAND_NAME_MAX)
  private String brandName;

  private String brandNameJp;

  private String brandImage;

  private Boolean isPopular;

  @NotNull
  private Boolean isVisible;

  @JsonIgnore
  private Integer userId;
}
