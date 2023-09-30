package com.laplace.api.common.dto;

import com.laplace.api.common.constants.TextLength;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class PolicyDetailsDto {

  @Size(min = TextLength.REG_POLICY_PERIOD_MIN, max = TextLength.REG_POLICY_PERIOD_MAX)
  private String period;

  private Integer policyPriority;

  private Integer logicId;

  private Integer logicValue;

  private Integer policyTypeId;

}
