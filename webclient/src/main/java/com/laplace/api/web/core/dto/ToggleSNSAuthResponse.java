package com.laplace.api.web.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToggleSNSAuthResponse {

  private Integer snsType;
  private boolean toggleVal;
}
