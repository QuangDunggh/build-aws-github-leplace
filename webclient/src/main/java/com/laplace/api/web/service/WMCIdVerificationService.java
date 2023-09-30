package com.laplace.api.web.service;

import com.laplace.api.common.dto.request.DocumentUploadDto;
import com.laplace.api.common.dto.request.StripeAccountRequestDTO;
import com.laplace.api.common.dto.response.BankAccountInfo;
import com.stripe.exception.StripeException;

public interface WMCIdVerificationService {

  String createStripeAccount(StripeAccountRequestDTO stripeAccountRequestDTO) throws StripeException;

  Boolean uploadVerificationDocument(DocumentUploadDto documentUploadDto) throws StripeException;

  BankAccountInfo findBankAccount();

  void checkAndModifyVerificationStatus(Integer userId) throws StripeException;
}
