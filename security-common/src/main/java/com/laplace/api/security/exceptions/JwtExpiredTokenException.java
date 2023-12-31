package com.laplace.api.security.exceptions;

import com.laplace.api.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;


public class JwtExpiredTokenException extends AuthenticationException {

  private static final long serialVersionUID = -5959543783324224864L;

  private final JwtToken token;

  public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
    super(msg, t);
    this.token = token;
  }

  public String token() {
    return this.token.getToken();
  }
}
