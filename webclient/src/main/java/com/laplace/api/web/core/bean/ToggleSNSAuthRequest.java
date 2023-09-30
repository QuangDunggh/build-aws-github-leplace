package com.laplace.api.web.core.bean;

import lombok.Data;

@Data
public class ToggleSNSAuthRequest {

  private Integer snsType;
  private String token;
  private String twitterVerifier;
}