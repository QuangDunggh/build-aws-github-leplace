package com.laplace.api.security.auth.jwt.extractor;

import com.laplace.api.security.config.JwtSettings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;


@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

  private final JwtSettings jwtSettings;

  @Autowired
  public JwtHeaderTokenExtractor(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }


  @Override
  public String extract(String header) {
    if (StringUtils.isBlank(header)) {
      throw new AuthenticationServiceException("Authorization header cannot be blank!");
    }

    if (header.length() < jwtSettings.getPrefix().length()) {
      throw new AuthenticationServiceException("Invalid authorization header size.");
    }

    return header.substring(jwtSettings.getPrefix().length());
  }
}
