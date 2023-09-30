package com.laplace.api.security.social.service;

import lombok.Data;

@Data
public class TokenStatus {

  private boolean valid;
  private String userId;
  private String message;
  private String email;
}
