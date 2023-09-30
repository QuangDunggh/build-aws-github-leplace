package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum DeliveryFeeBearer {
  BUYER("バイヤーが負担"),
  SELLER("セラーが負担");

  private String value;

  DeliveryFeeBearer(String value) {
    this.value = value;
  }

  private static Supplier<Stream<DeliveryFeeBearer>>
  deliveryFeeBearerSupplier() {
    return () -> Stream.of(DeliveryFeeBearer.values());
  }

  public static DeliveryFeeBearer forName(String deliveryFeeBearer) {
    return deliveryFeeBearerSupplier().get()
        .filter(condition -> condition.name().equals(deliveryFeeBearer))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + deliveryFeeBearer + " value!"));
  }

  public String getValue() {
    return value;
  }

}
