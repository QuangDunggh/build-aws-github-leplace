package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserDetailsInfo {
  private String firstNameKana;
  private String lastNameKana;
  private String userName;
  private Long dateOfBirth;
  private Integer day;
  private Integer month;
  private Integer year;
  private String phoneNumber;
  private AppUserAddressBasicInfo address;
}
