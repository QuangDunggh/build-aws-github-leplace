package com.laplace.api.security.config;

public final class SecurityConfigConstants {

  public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
  public static final String CMS_REFRESH_TOKEN_URL = "/p/cms/refresh";
  public static final String PUBLIC_URL_PREFIX = "/p/**";
  public static final String CLIENT_REFRESH_TOKEN_URL = "/p/client/refresh";
  public static final String AUTHENTICATED_URL_PREFIX = "/a/**";

  private SecurityConfigConstants() {
  }
}
