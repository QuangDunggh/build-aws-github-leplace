package com.laplace.api.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

@Slf4j
public class ResourceFileLoader {

  private final Resource resource;

  @Autowired
  public ResourceFileLoader(String fileName) {
    this.resource = new ClassPathResource(fileName);
  }

  public String toString() {
    String data = null;
    try (InputStream inputStream = resource.getInputStream()) {
      byte[] jsonData = FileCopyUtils.copyToByteArray(inputStream);
      data = new String(jsonData, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error("++File to String conversion error: " + e.getMessage());
    }
    return data;
  }
}
