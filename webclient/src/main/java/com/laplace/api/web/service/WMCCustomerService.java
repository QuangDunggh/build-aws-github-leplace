package com.laplace.api.web.service;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.pay.jp.CustomerService;
import com.laplace.api.common.util.BaseResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class WMCCustomerService {

    private final CustomerService customerService;

    public WMCCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public BaseResponse getClientSecretForSetupIntent(Integer userId) {
        return BaseResponse.builder()
                .responseType(ResponseType.RESULT)
                .message(Collections.singleton(ApplicationConstants.CREATED_MSG))
                .result(customerService.getSetUpIntentForSavingPaymentInfo(userId))
                .code(ApplicationConstants.CREATED_SUCCESS_CODE)
                .build();
    }

    public BaseResponse getCustomerPaymentDetailsInfo(Integer userId) {
        return BaseResponse.builder()
                .responseType(ResponseType.RESULT)
                .message(Collections.singleton(ApplicationConstants.OK_MSG))
                .result(customerService.getCustomerPaymentMethods(userId))
                .code(ApplicationConstants.SUCCESS_CODE)
                .build();
    }

    public BaseResponse detachCustomerPaymentMethod(String paymentMethodId, Integer userId) {
        return BaseResponse.builder()
                .responseType(ResponseType.RESULT)
                .message(Collections.singleton(ApplicationConstants.OK_MSG))
                .result(customerService.detachCustomerPaymentMethod(paymentMethodId, userId))
                .code(ApplicationConstants.SUCCESS_CODE)
                .build();
    }
}
