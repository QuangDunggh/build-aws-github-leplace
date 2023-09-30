package com.laplace.api.common.configuration.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class LaplaceS3Client {

  private AmazonS3 s3client;

  @Setter(AccessLevel.NONE)
  @Value("${spring.profiles.active}")
  private String applicationProfile;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.endpoint.url}")
  private String endpointUrl;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.cms.bucket}")
  private String mediaBucketName;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.region}")
  private String region;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.credential.profile: ''}")
  private String credentialProfile;

  @Setter(AccessLevel.NONE)
  @Value("${aws.cloudFront.url}")
  private String cloudFrontURL;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.laplace.resource.file-name}")
  private String laplaceResourceFileName;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.uuork.pre-signed.url.expiration.in-minutes}")
  private Long presignedUrlExpirationInMinutes;

  private AWSCredentialsProvider awsCredentialsProvider;

}
