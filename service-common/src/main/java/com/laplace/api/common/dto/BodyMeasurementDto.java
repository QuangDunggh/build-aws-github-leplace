package com.laplace.api.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyMeasurementDto {

  @JsonIgnore
  private Integer userId;
  private String bodyPart;
  private Double value;
  private String unit;
}
