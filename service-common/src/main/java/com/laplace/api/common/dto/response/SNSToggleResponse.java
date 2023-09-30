package com.laplace.api.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SNSToggleResponse {

  private Integer snsType;
  private Boolean toggleVal;
}
