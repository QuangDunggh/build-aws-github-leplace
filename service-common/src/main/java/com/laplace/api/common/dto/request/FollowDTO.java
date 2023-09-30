package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ErrorCode;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowDTO {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Boolean follow;
}
