package com.laplace.api.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefsDto {

  private Integer paymentId;
  private String chargeId;
  private Integer price;
  private Integer refundAmount;
  private Integer actualRefundAmount;
  private String type;
  private Long timeStamp;
  private Boolean capture;
}
