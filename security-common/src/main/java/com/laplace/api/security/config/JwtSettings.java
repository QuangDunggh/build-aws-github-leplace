package com.laplace.api.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:security-${spring.profiles.active}.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtSettings {

  private Integer tokenExpirationTime;
  private String tokenIssuer;
  private String tokenSigningKey;
  private Integer refreshTokenExpTime;
  private String header;
  private String prefix;
  private String refreshTokenSalt;
}
