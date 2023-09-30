package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.BankAccountType;
import com.stripe.model.BankAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountInfo {
  private String bankName;
  private String bankCode;
  private String branchName;
  private String branchCode;
  private BankAccountType typeOfAccount;
  private String accountHolderType;
  private String country;
  private String currency;
  private String accountNumber;
  private String accountHolderName;

  public static BankAccountInfo make(BankAccount bankAccount) {
    return bankAccount == null ? null : BankAccountInfo.builder()
            .country(bankAccount.getCountry())
            .currency(bankAccount.getCurrency())
            .accountHolderName(bankAccount.getAccountHolderName())
            .accountHolderType(bankAccount.getAccountHolderType())
            .accountNumber(bankAccount.getLast4())
            .bankName(bankAccount.getBankName())
            .branchCode(bankAccount.getRoutingNumber())
            .typeOfAccount(BankAccountType.GENERAL)
            .build();
  }
}
