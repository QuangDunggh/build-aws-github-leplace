package com.laplace.api.cms.core.bean;

import lombok.Data;

@Data
public class AuthenticationRequest {

  private String email;
  private String password;
  private String accessToken;
}
