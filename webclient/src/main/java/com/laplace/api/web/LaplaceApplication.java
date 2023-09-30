package com.laplace.api.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class LaplaceApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(LaplaceApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(LaplaceApplication.class);
  }
}
