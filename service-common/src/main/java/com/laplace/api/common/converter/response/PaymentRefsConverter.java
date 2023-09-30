package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.response.PaymentRefsDto;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.util.DateUtil;
import javax.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PaymentRefsConverter implements Converter<Payment, PaymentRefsDto> {

  @Override
  public PaymentRefsDto convert(@Nonnull Payment payment) {
    return PaymentRefsDto.builder()
        .paymentId(payment.getId())
        .chargeId(payment.getChargeId())
        .price(payment.getOriginalAmount())
        .refundAmount(payment.getRefundAmount())
        .actualRefundAmount(payment.getActualRefundAmount())
        .type(payment.getPaymentType().name())
        .timeStamp(null != payment.getCreatedAt() ? DateUtil.toEpochMilli(payment.getCreatedAt()) : null)
        .build();
  }
}
