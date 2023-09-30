package com.laplace.api.security.social.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:social-config-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "social.twitter")
@Data
public class TwitterConfig {

  private String appName;
  private String consumerApiKey;
  private String consumerApiSecret;
  private String loginCallbackUrl;
  private String signUpCallbackUrl;
}
