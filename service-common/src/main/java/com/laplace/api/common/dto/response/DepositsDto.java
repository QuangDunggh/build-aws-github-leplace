package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.util.DateUtil;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Builder
@Data
public class DepositsDto {

  private String itemName;
  private String itemId;
  private String brandName;
  private Long paymentTime;
  private Integer itemPrice;
  private Integer shippingCharge;
  private Integer processingFee;
  private Integer optionPrice;
  private Integer sellerReceivingAmount;
  private Integer totalPrice;

  public static DepositsDto from(Order order, Item item) {
    DepositsDto depositsDto = DepositsDto.builder()
        .brandName(item.getBrandName())
        .itemId(item.getItemId())
        .itemName(item.getItemName())
        .paymentTime(DateUtil.toEpochMilli(order.getCreatedAt()))
        .build();

    Optional<Payment> paymentOptional = order.getPayments().stream()
        .filter(pay -> pay.getPaymentRefId().equals(
            ApplicationConstants.PAYMENT_PARENT_REF_ID)).findAny();
    if (paymentOptional.isEmpty()) {
      return depositsDto;
    }
    Payment payment = paymentOptional.get();
    depositsDto.setItemPrice(payment.getItemOriginalPrice());
    depositsDto.setOptionPrice(payment.getGiftWrappingPrice());
    depositsDto.setShippingCharge(
        ObjectUtils.nullSafeEquals(item.getDeliveryFeeBearer(), DeliveryFeeBearer.SELLER)
            ? payment.getDeliveryFee() : ApplicationConstants.VALUE_ZERO);
    depositsDto.setProcessingFee(payment.getProcessingFee());
    depositsDto.setTotalPrice(payment.getOriginalAmount());
    depositsDto.setSellerReceivingAmount(payment.getSellerAmount());
    return depositsDto;
  }
}
