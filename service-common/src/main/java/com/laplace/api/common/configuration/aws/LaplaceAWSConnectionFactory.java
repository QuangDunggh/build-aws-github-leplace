package com.laplace.api.common.configuration.aws;

import static com.laplace.api.common.constants.ApplicationConstants.APPLICATION_LOCAL_PROFILE;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LaplaceAWSConnectionFactory {

  private final LaplaceS3Client laplaceS3Client;
  private final LaplaceLambdaClient laplaceLambdaClient;

  @Autowired
  public LaplaceAWSConnectionFactory(LaplaceS3Client laplaceS3Client,
      LaplaceLambdaClient laplaceLambdaClient) {
    this.laplaceS3Client = initializeAmazonS3Client(laplaceS3Client);
    this.laplaceLambdaClient = initializeAmazonLambdaClient(laplaceLambdaClient);
  }

  private AWSCredentialsProvider awsCredentialsProvider() {
    return new DefaultAWSCredentialsProviderChain();
  }

  private LaplaceS3Client initializeAmazonS3Client(LaplaceS3Client laplaceS3Client) {
    AmazonS3 s3client = AmazonS3ClientBuilder.standard()
        .withRegion(laplaceS3Client.getRegion())
        .withCredentials(getProvider(laplaceS3Client.getApplicationProfile(),
            laplaceS3Client.getCredentialProfile()))
        .withPathStyleAccessEnabled(true)
        .build();
    laplaceS3Client.setS3client(s3client);
    return laplaceS3Client;
  }

  private LaplaceLambdaClient initializeAmazonLambdaClient(LaplaceLambdaClient lambdaClient) {
    AWSLambdaAsync lambda = AWSLambdaAsyncClientBuilder.standard()
        .withRegion(lambdaClient.getRegion())
        .withClientConfiguration(new ClientConfiguration().withTcpKeepAlive(true))
        .build();
    lambdaClient.setLambdaClient(lambda);
    return lambdaClient;
  }

  private AWSCredentialsProvider getProvider(String profileName, String credentialProfile) {
    if (profileName.equalsIgnoreCase(APPLICATION_LOCAL_PROFILE)) {
      return (new ProfileCredentialsProvider(credentialProfile));
    } else {
      return awsCredentialsProvider();
    }
  }

}
