package com.laplace.api.common.util;

import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;

import com.laplace.api.common.constants.ApplicationConstants;
import java.util.Objects;

public class PaymentUtil {

  public static Integer calculateProcessingFee(Integer itemPrice, Integer processingRate) {
    itemPrice = Objects.isNull(itemPrice) ? 0 : itemPrice;
    processingRate = Objects.isNull(processingRate) ? 0 : processingRate;
    return itemPrice * processingRate / ApplicationConstants.PERCENTAGE;
  }

  public static int calculateDiscountPrice(int newPrice, int oldPrice) {
    int percentage = oldPrice == VALUE_ZERO ? VALUE_ZERO
        : ((oldPrice - newPrice) * ApplicationConstants.PERCENTAGE / oldPrice);
    return percentage < ApplicationConstants.ONE ? VALUE_ZERO : percentage;
  }
}
