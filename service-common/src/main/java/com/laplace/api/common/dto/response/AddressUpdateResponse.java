package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressUpdateResponse {

  private Integer id;
  private String name;
  private String reading;
  private String zipCode;
  private Integer state;
  private String city;
  private String line1;
  private String line2;
  private String phone;
  private Long updatedAt;
}
