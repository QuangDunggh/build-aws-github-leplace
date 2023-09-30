package com.laplace.api.common.dto;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDTO {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Boolean isCoverImage;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @URL(message = ErrorCode.INVALID_ARGUMENT)
  private String imageUrl;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @Min(value = ApplicationConstants.VALUE_ONE, message = ErrorCode.INVALID_ARGUMENT)
  private Integer number;

}
