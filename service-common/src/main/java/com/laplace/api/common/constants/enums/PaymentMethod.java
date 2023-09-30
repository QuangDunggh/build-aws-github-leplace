package com.laplace.api.common.constants.enums;

import com.laplace.api.common.constants.Messages;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum PaymentMethod {
  CARD("card"), CUSTOMER("customer"), TOKEN("token"), CUSTOMER_CARD("source");

  private String value;

  PaymentMethod(String value) {
    this.value = value;
  }

  /**
   * Get payment source params from name. Validate params name
   *
   * @param value name
   * @return PayJPParamsType
   */
  public static PaymentMethod fromName(String value) {
    return Stream.of(PaymentMethod.values()).filter(type -> type.value == value)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, value,
                CoordinateType.class.getName())));
  }
}
