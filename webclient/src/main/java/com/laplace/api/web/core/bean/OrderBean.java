package com.laplace.api.web.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.web.core.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBean {

  private String orderId;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer shippingAddressId;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String paymentMethodId;

  private String coupon;

  private GiftWrappingType giftWrappingOptions;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String shippingName;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String shippingKatakanaName;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String shippingPhoneNumber;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String shippingEmail;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private String itemId;

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private OrderType type;

  private String paymentIntent;

  private Integer extendPeriod;

  private boolean confirm;

  @JsonIgnore
  private Integer deliveryFee;
}
