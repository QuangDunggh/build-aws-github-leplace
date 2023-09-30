package com.laplace.api.security.social.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookVerificationResponse {

  private VerificationData data;
  private String id;
  private String email;
  private FacebookError error;
}

