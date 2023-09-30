package com.laplace.api.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryLanguageDto {

  CategoryEnDto en;
  CategoryJpDto jp;
}
