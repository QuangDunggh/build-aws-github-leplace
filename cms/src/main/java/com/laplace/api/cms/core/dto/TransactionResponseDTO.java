package com.laplace.api.cms.core.dto;

import java.util.List;
import lombok.Data;

@Data
public class TransactionResponseDTO {

  private Long total = 0L;
  private Integer totalPage = 0;
  private Long sumOfPrices = 0L;
  private List<PaymentResponseDto> paymentList;
}
