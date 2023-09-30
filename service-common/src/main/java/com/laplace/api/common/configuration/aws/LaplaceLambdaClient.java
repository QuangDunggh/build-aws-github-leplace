package com.laplace.api.common.configuration.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class LaplaceLambdaClient {

  private AWSLambdaAsync lambdaClient;

  @Setter(AccessLevel.NONE)
  @Value("${spring.profiles.active}")
  private String applicationProfile;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.region}")
  private String region;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.credential.profile: ''}")
  private String credentialProfile;

  private AWSCredentialsProvider awsCredentialsProvider;
}
