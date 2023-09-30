package com.laplace.api.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneOfInterestDto {

  @JsonIgnore
  private Integer userId;

  private Integer tagAttributeId;

  private String image;

  private String name;

  private boolean selected;
}
