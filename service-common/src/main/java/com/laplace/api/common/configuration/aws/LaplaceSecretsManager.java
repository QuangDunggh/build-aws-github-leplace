package com.laplace.api.common.configuration.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.SecretsManagerKeyDto;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaplaceSecretsManager {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Value("${aws.s3.region}")
  private String currentRegion;

  @Value("${aws.secrets.manager.secretId}")
  private String secretId;

  @Bean
  public SecretsManagerKeyDto getSecretsManagerKey() throws IOException {
    AWSCredentialsProvider awsCredentialsProvider;
    if (activeProfile.equalsIgnoreCase(ApplicationConstants.APPLICATION_LOCAL_PROFILE)) {
      awsCredentialsProvider = new ProfileCredentialsProvider();
    } else {
      awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();
    }

    AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
        .withRegion(currentRegion)
        .withCredentials(awsCredentialsProvider)
        .build();

    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
        .withSecretId(secretId);
    GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
    if (StringUtils.isBlank(getSecretValueResult.getSecretString())) {
      throw new RuntimeException("Secrets manager secret string is empty");
    }

    String secret = getSecretValueResult.getSecretString();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    SecretsManagerKeyDto secretsManagerKeyDto = objectMapper
        .readValue(secret, SecretsManagerKeyDto.class);
    SecretsManagerKeyDto.validate(secretsManagerKeyDto);
    return secretsManagerKeyDto;
  }
}
