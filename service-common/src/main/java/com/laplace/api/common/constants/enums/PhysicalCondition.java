package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum PhysicalCondition {
  NEW("新品/未使用"),
  MINT_CONDITION("美品"),
  GOOD("良好"),
  NORMAL("普通に使える"),
  WORN_OUT("使用感あり");

  private String value;

  PhysicalCondition(String value) {
    this.value = value;
  }

  private static Supplier<Stream<PhysicalCondition>>
  physicalConditionSupplier() {
    return () -> Stream.of(PhysicalCondition.values());
  }

  public static PhysicalCondition forName(String physicalCondition) {
    return physicalConditionSupplier().get()
        .filter(condition -> condition.name().equals(physicalCondition))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + physicalCondition + " value!"));
  }

  public String getValue() {
    return value;
  }
}
