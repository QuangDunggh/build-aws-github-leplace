package com.laplace.api.security.auth.jwt.verifier;

public interface TokenVerifier {

  boolean verify(String jti);
}
