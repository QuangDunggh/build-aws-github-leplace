package com.laplace.api.gateway;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@SpringBootApplication(scanBasePackages = {"com.laplace.api.common", "com.laplace.api.security",
    "com.laplace.api.gateway", "com.laplace.api.cms", "com.laplace.api.web", "com.laplace.api.file"})
@EntityScan(basePackages = {"com.laplace.api.common", "com.laplace.api.cms.model",
    "com.laplace.api.security.model"})
@EnableJpaRepositories(basePackages = {"com.laplace.api.common.repository.db"})
@EnableRedisRepositories(basePackages = {"com.laplace.api.common.repository.redis"})
@EnableElasticsearchRepositories(basePackages = {"com.laplace.api.common.repository.elasticsearch"})
@EnableRetry
public class MainApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    System.setProperty("user.timezone", "UTC");
    SpringApplication.run(MainApplication.class, args);
  }

  @PostConstruct
  public void init(){
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(MainApplication.class);
  }
}
