package com.laplace.api.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicInfoResponseDto {

  private Integer id;
  private String firstName;
  private String familyName;
  private String name;
  private String phoneNumber1;
  private String phoneNumber2;
  private String profileImage;
  private Integer shopId;
  private String email;
}
