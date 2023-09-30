package com.laplace.api.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties
public class SecretsManagerKeyDto {

  private String secret;
  private String tenant;
  private String apiAccessKey;
  private String lambdaKey;

  public static void validate(SecretsManagerKeyDto secretsManagerKeyDto) {
    throwIfBlank(secretsManagerKeyDto.getSecret(), "secret");
    throwIfBlank(secretsManagerKeyDto.getTenant(), "tenant");
    throwIfBlank(secretsManagerKeyDto.getApiAccessKey(), "apiAccessKey");
    throwIfBlank(secretsManagerKeyDto.getApiAccessKey(), "lambdaKey");
  }

  private static void throwIfBlank(String str, String keyName) {
    if (StringUtils.isBlank(str)) {
      throw new RuntimeException(keyName + ": is blank in " + SecretsManagerKeyDto.class.getName());
    }
  }
}
