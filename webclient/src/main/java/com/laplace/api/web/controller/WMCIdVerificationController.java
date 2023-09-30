package com.laplace.api.web.controller;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.dto.request.DocumentUploadDto;
import com.laplace.api.common.dto.request.StripeAccountRequestDTO;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCIdVerificationService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping(APIEndPoints.ID_VERIFICATION)
@PreAuthorize(ApplicationConstants.AUTHORITY_APP_USER)
public class WMCIdVerificationController {

  private final WMCIdVerificationService wmcIdVerificationService;

  @Autowired
  public WMCIdVerificationController(
      WMCIdVerificationService wmcIdVerificationService) {
    this.wmcIdVerificationService = wmcIdVerificationService;
  }

  @PostMapping("/account")
  public BaseResponse createStripeAccount(
      @Valid @RequestBody StripeAccountRequestDTO stripeAccountRequestDTO) throws StripeException {
    return BaseResponse.builder()
            .responseType(ResponseType.RESULT)
            .message(Collections.singleton(ApplicationConstants.CREATED_MSG))
            .result(wmcIdVerificationService.createStripeAccount(stripeAccountRequestDTO))
            .code(ApplicationConstants.CREATED_SUCCESS_CODE)
            .build();
  }

  @PutMapping("/account")
  public BaseResponse updateStripeAccount(
          @Valid @RequestBody DocumentUploadDto documentUploadDto) throws StripeException {
    return BaseResponse.builder()
            .responseType(ResponseType.RESULT)
            .message(Collections.singleton(ApplicationConstants.CREATED_MSG))
            .result(wmcIdVerificationService.uploadVerificationDocument(documentUploadDto))
            .code(ApplicationConstants.CREATED_SUCCESS_CODE)
            .build();
  }

  @GetMapping(APIEndPoints.BANKS)
  public BaseResponse findBankAccount() {
    return BaseResponse.create(wmcIdVerificationService.findBankAccount());
  }
}
