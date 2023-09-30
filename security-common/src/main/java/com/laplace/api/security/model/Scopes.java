package com.laplace.api.security.model;

public enum Scopes {
  REFRESH_TOKEN;

  public String authority() {
    return "ROLE_" + this.name();
  }
}
