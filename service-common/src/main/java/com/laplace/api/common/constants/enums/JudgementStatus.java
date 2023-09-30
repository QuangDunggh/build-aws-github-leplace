package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum JudgementStatus {
  NOT_VERIFIED("未確認"),
  GENUINE("真作"),
  FAKE("偽");

  private String value;

  JudgementStatus(String value) {
    this.value = value;
  }

  private static Supplier<Stream<JudgementStatus>>
  judgementStatusSupplier() {
    return () -> Stream.of(JudgementStatus.values());
  }

  public static JudgementStatus forName(String status) {
    return judgementStatusSupplier().get()
        .filter(judgementStatus -> judgementStatus.name().equals(status))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + status + " value!"));
  }

  public String getValue() {
    return value;
  }
}
