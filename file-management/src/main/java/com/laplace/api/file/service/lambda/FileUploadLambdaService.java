package com.laplace.api.file.service.lambda;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.configuration.aws.LaplaceAWSConnectionFactory;
import com.laplace.api.file.payload.response.FileUploadDetails;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileUploadLambdaService {

  private AWSLambdaAsync lambda;
  private final LaplaceAWSConnectionFactory awsConnectionFactory;
  private final ObjectMapper objectMapper;

  @Value("${lambda.s3.presigned}")
  private String presignedUrlLambda;

  @Autowired
  public FileUploadLambdaService(
      LaplaceAWSConnectionFactory awsConnectionFactory, ObjectMapper objectMapper) {
    this.awsConnectionFactory = awsConnectionFactory;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  void init() {
    this.lambda = this.awsConnectionFactory.getLaplaceLambdaClient().getLambdaClient();
  }

  public JsonNode generatePresignedUrl(FileUploadDetails details) throws JsonProcessingException {
    try {
      String payload = objectMapper.writeValueAsString(details);

      InvokeResult result = lambda.invoke(new InvokeRequest().withFunctionName(presignedUrlLambda)
          .withPayload(payload));
      String resultJSON = new String(result.getPayload().array(), StandardCharsets.UTF_8);
      log.info("invoke result: {}", resultJSON);
      Integer statusCode = result.getStatusCode();
      if (statusCode == 200) {
        JsonNode responseObj = objectMapper.readTree(resultJSON);
        return responseObj.get("body");
      } else {
        throw new RuntimeException("File upload presigned url generation failed");
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }
}
