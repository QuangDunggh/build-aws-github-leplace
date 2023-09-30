package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum WithdrawalReason {
  NO_PRODUCT_TO_WORRY("気になる商品がない"),
  RELATED_TO_DELIVERY("配送に関する内容"),
  RELATED_TO_FEES("手数料に関する内容"),
  USER_WHO_IS_USING("利用しているユーザーに関する内容"),
  OWN_CIRCUMSTANCES("ご自身の事情"),
  OTHER("その他");

  private String value;

  WithdrawalReason(String value) {
    this.value = value;
  }

  private static Supplier<Stream<WithdrawalReason>>
  withdrawalReasonSupplier() {
    return () -> Stream.of(WithdrawalReason.values());
  }

  public static WithdrawalReason forName(String withdrawalReason) {
    return withdrawalReasonSupplier().get()
        .filter(condition -> condition.name().equals(withdrawalReason))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + withdrawalReason + " value!"));
  }

  public String getValue() {
    return value;
  }
}
