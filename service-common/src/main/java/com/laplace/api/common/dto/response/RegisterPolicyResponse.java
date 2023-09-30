package com.laplace.api.common.dto.response;

import com.laplace.api.common.dto.PolicyDetailsDto;
import java.util.List;
import lombok.Data;

@Data
public class RegisterPolicyResponse {

  private Integer id;

  private String summary;

  private Integer categoryId;

  private String categoryName;

  private List<PolicyDetailsDto> cancelPolicies;

  private List<PolicyDetailsDto> delayPolicies;
}
