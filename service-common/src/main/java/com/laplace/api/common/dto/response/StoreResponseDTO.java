package com.laplace.api.common.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreResponseDTO implements Serializable {

  private Integer id;
  private String name;
  private String location;
  private String city;
  private String zip;
  private Integer stateId;
  private String line1;
  private String line2;
  private long createdAt;
}