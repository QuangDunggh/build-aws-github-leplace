package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserProfileResponseDto {
  private AppUserBasicInfo basicInfo;
  private AppUserDetailsInfo detailsInfo;
  private BankAccountInfo bankAccountInfo;
}
