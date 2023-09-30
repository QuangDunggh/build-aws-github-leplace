package com.laplace.api.web.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDTO {

  private String accessToken;
  private String accessId;
  private String email;
  private String firstName;
  private String familyName;
  private String profileImage;
  private boolean profileComplete;
  private boolean isActive;
}
