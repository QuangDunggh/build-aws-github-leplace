package com.laplace.api.web.controller.internal;

import com.laplace.api.common.configuration.aws.LaplaceSecretsManager;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.OrderService;
import com.laplace.api.web.service.WMCIdVerificationService;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.laplace.api.common.constants.ApplicationConstants.LAPLACE_API_ACCESS_KEY;
import static com.laplace.api.common.constants.ApplicationConstants.LAPLACE_LAMBDA_ACCESS_KEY;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.LAMBDA)
public class LambdaController {

  private final LaplaceSecretsManager secretsManager;
  private final OrderService orderService;
  private final WMCIdVerificationService wmcIdVerificationService;

  @Autowired
  public LambdaController(LaplaceSecretsManager secretsManager, OrderService orderService,
                          WMCIdVerificationService wmcIdVerificationService) {
    this.secretsManager = secretsManager;
    this.orderService = orderService;
    this.wmcIdVerificationService = wmcIdVerificationService;
  }

  @PatchMapping(APIEndPoints.CANCEL_SALE_ITEM)
  public BaseResponse cancelOnSaleOverItem(@RequestHeader(LAPLACE_API_ACCESS_KEY) String apiKey,
      @RequestHeader(LAPLACE_LAMBDA_ACCESS_KEY) String lambdaKey,
      @PathVariable("id") String itemId)
          throws IOException, StripeException {
    validateKey(apiKey, lambdaKey);
    orderService.returnOnSaleItem(itemId, lambdaKey);
    return BaseResponse.create(ApplicationConstants.SUCCESS);
  }

  @PatchMapping(APIEndPoints.UPDATE_ID)
  public BaseResponse updateIdVerificationStatus(@RequestHeader(LAPLACE_API_ACCESS_KEY) String apiKey,
                                                 @RequestHeader(LAPLACE_LAMBDA_ACCESS_KEY) String lambdaKey,
                                                 @PathVariable("id") Integer userId)
          throws IOException, StripeException {
    validateKey(apiKey, lambdaKey);
    wmcIdVerificationService.checkAndModifyVerificationStatus(userId);
    return BaseResponse.create(ApplicationConstants.SUCCESS);
  }

  private void validateKey(String apiKey, String lambdaKey) throws IOException {
    if (!StringUtils.equals(apiKey, secretsManager.getSecretsManagerKey().getApiAccessKey())
        || !StringUtils.equals(lambdaKey, secretsManager.getSecretsManagerKey().getLambdaKey())) {
      throwApplicationException(ResultCodeConstants.AUTH_FAILURE);
    }
  }
}
