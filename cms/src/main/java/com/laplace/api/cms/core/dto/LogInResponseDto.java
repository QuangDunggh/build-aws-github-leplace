package com.laplace.api.cms.core.dto;

import java.util.Set;
import lombok.Data;

@Data
public class LogInResponseDto {

  String accessToken;
  Set<String> role;
  Set<String> allowedEntities;
  String accessId;
}
