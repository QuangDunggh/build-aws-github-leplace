package com.laplace.api.common.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandResponseDTO implements Serializable {

  private Integer brandId;
  private String brandName;
  private String brandNameJp;
  private String brandImage;
  private Boolean isVisible;
  private Boolean isPopular;
  private ZonedDateTime lastPopularAt;
}
