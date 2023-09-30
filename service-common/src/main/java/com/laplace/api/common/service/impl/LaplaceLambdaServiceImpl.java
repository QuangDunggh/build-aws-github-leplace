package com.laplace.api.common.service.impl;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.configuration.aws.LaplaceAWSConnectionFactory;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.service.LaplaceLambdaService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LaplaceLambdaServiceImpl implements LaplaceLambdaService {

  private final LaplaceAWSConnectionFactory awsConnectionFactory;
  private final ObjectMapper objectMapper;
  private AWSLambdaAsync lambda;

  @Value("${lambda.item.discount.notify}")
  private String itemDiscountNotifyLambda;

  @Value("${lambda.admin.notice}")
  private String adminNoticeLambda;

  @Autowired
  public LaplaceLambdaServiceImpl(
      LaplaceAWSConnectionFactory awsConnectionFactory, ObjectMapper objectMapper) {
    this.awsConnectionFactory = awsConnectionFactory;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  void init() {
    this.lambda = this.awsConnectionFactory.getLaplaceLambdaClient().getLambdaClient();
  }

  @Override
  public void itemDiscountNotifyHandler(Notification data) {
    try {
      String payload = objectMapper.writeValueAsString(data);
      invoke(payload, itemDiscountNotifyLambda);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void adminNoticeHandler() {
    String payload = StringUtils.EMPTY_JSON;
    invoke(payload, adminNoticeLambda);
  }

  private void invoke(String payload, String functionName) {
    try {
      lambda.invokeAsync(new InvokeRequest().withFunctionName(functionName).withPayload(payload));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
