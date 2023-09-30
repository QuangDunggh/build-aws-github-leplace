package com.laplace.api.security.social.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:social-config-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "social.facebook")
@Data
public class FacebookConfig {

  private String appId;
  private String appName;
  private String appSecret;
  private String baseUrl;
  private String accessTokenValidationUrl;
  private String longLiveTokenUrl;
}

