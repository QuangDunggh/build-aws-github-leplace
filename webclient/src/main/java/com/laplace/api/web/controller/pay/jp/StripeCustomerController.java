package com.laplace.api.web.controller.pay.jp;

import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.core.bean.CustomerBean;
import com.laplace.api.web.service.WMCCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.AUTH_FAILURE;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@RestController
@RequestMapping(APIEndPoints.CUSTOMER)
public class StripeCustomerController {

  private final WMCCustomerService customerService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public StripeCustomerController(WMCCustomerService customerService, AuthenticationFacade authenticationFacade) {
    this.customerService = customerService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping("/setup-intent")
  public BaseResponse getSetupIntentForSavingPaymentInfo() {
    return customerService.getClientSecretForSetupIntent(getUserId());
  }

  @PutMapping
  public BaseResponse detachCustomerPaymentInfo(@RequestBody CustomerBean customerBean){
    return customerService.detachCustomerPaymentMethod(customerBean.getPaymentMethod(), getUserId());
  }

  @GetMapping
  public BaseResponse getCustomerDetailsInfo() {
    return customerService.getCustomerPaymentDetailsInfo(getUserId());
  }

  private Integer getUserId() {
    return authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(AUTH_FAILURE)).getUserId();
  }
}
