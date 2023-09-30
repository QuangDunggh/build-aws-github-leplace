package com.laplace.api.common.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

  private String id;
  private Integer clientId;
  private Integer userId;
  private String name;
  private String reading;
  private String country;
  private String zip;
  private Integer stateId;
  private String city;
  private String line1;
  private String line2;
  private String tel1;
  private String tel2;
  private Date createdAt;
  private Date updatedAt;
  private String location;
  private Integer storageId;
  private String prefectures;
  private String address;
}