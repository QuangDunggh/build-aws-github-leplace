package com.laplace.api.security.endpoint;

import static com.laplace.api.security.config.SecurityConfigConstants.CLIENT_REFRESH_TOKEN_URL;
import static com.laplace.api.security.config.SecurityConfigConstants.CMS_REFRESH_TOKEN_URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class RefreshTokenEndpoint {

  @PostMapping(value = {CLIENT_REFRESH_TOKEN_URL,
      CMS_REFRESH_TOKEN_URL})
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
