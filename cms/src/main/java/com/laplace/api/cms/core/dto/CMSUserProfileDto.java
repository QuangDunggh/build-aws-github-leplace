package com.laplace.api.cms.core.dto;

import javax.validation.Valid;
import lombok.Data;

@Valid
@Data
public class CMSUserProfileDto {

  private Integer userId;

  private String profileImageUrl;

  private String favIconImageUrl;

  private String name;
}
