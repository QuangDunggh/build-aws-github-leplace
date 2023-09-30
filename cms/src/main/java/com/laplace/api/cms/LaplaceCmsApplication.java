package com.laplace.api.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


public class LaplaceCmsApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(LaplaceCmsApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(LaplaceCmsApplication.class);
  }
}
