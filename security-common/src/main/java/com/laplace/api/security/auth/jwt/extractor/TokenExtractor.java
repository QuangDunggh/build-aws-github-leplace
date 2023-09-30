package com.laplace.api.security.auth.jwt.extractor;

public interface TokenExtractor {

  String extract(String payload);
}
