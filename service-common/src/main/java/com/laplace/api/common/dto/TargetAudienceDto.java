package com.laplace.api.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TargetAudienceDto {

  CategoryLanguageDto Men;
  CategoryLanguageDto Women;
  CategoryLanguageDto All;
}
