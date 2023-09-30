package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDTO {

  private String id;

  private Integer clientId;

  private Integer goodsId;

  private String productCode;

  private String measurementLength;

  private String measurementShoulder;

  private String measurementWidth;

  private String measurementSleeve;

  private String measurementWaist;

  private String measurementInseam;

  private String measurementRise;

  private String measurementHem;

  private String measurementHip;

  private String measurementFree;

  private Long createdAt;

  private Long updatedAt;

  private ColorDTO color;

  private SizeResponseDTO size;
}
