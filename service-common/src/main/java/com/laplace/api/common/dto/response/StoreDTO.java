package com.laplace.api.common.dto.response;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDTO implements Serializable {

  private String name;

  private String location;

  private String city;

  private String zip;

  private Integer stateId;

  private String line1;

  private String line2;

  private Integer storageId;

  private Date createdAt;

  private Date updatedAt;
}
