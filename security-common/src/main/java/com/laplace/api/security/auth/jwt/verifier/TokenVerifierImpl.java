package com.laplace.api.security.auth.jwt.verifier;


import com.laplace.api.security.config.JwtSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenVerifierImpl implements TokenVerifier {

  private final JwtSettings settings;

  @Autowired
  public TokenVerifierImpl(JwtSettings settings) {
    this.settings = settings;
  }

  @Override
  public boolean verify(String jti) {
    return settings.getRefreshTokenSalt().equals(jti);
  }
}
