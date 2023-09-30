package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.WithdrawalReason;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
public class AccountWithdrawRequestDto {
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private List<WithdrawalReason> reason;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  private String detailsReason;
  @JsonIgnore
  private Integer userId;
}
