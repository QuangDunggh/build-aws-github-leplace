package com.laplace.api.common.dto.request;

import com.laplace.api.common.constants.TextLength;
import com.laplace.api.common.dto.PolicyDetailsDto;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class PolicyRequestDto {

  @Size(max = TextLength.REG_POLICY_SUMMARY_MAX)
  private String summary;

  private Integer categoryId;

  private String categoryName;

  private List<PolicyDetailsDto> cancelPolicies;

  private List<PolicyDetailsDto> delayPolicies;
}