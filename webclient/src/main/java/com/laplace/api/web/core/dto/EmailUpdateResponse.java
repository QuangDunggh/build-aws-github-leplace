package com.laplace.api.web.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailUpdateResponse {

  private String email;
}
