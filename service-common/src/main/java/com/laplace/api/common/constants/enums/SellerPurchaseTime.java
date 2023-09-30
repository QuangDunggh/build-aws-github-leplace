package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum SellerPurchaseTime {
  LESS_THAN_THREE_MONTH("3ヶ月未満"),
  LESS_THAN_SIX_MONTH("6ヶ月未満"),
  LESS_THAN_ONE_YEAR("2年未満"),
  LESS_THAN_THREE_YEAR("3年未満"),
  LESS_THAN_FIVE_YEAR("5年未満"),
  FIVE_YEAR_OR_MORE("5年以上");

  private String value;

  SellerPurchaseTime(String value) {
    this.value = value;
  }

  private static Supplier<Stream<SellerPurchaseTime>>
  sellerPurchaseTime() {
    return () -> Stream.of(SellerPurchaseTime.values());
  }

  public static SellerPurchaseTime forName(String purchaseTime) {
    return sellerPurchaseTime().get()
        .filter(sellerPurchaseTime -> sellerPurchaseTime.name().equals(purchaseTime))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + purchaseTime + " value!"));
  }

  public String getValue() {
    return value;
  }
}
