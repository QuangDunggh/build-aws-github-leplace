package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@Builder
public class CompanyInfoRequest {

  @Builder.Default
  private Role role = Role.USER;
  @Builder.Default
  private int start = ApplicationConstants.ZERO.intValue();
  @Builder.Default
  private int size = ApplicationConstants.DEFAULT_OFFSET.intValue();
  @Builder.Default
  private Sort.Direction direction = Sort.DEFAULT_DIRECTION;
}
