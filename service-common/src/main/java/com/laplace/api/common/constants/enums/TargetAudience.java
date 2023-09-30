package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum TargetAudience {
  WOMEN("ウィメンズ"),
  MEN("メンズ");

  private String value;


  @JsonCreator
  TargetAudience(String value) {
    this.value = value;
  }

  private static Supplier<Stream<TargetAudience>>
  targetOfUse() {
    return () -> Stream.of(TargetAudience.values());
  }

  public static TargetAudience forName(String target) {
    return targetOfUse().get()
        .filter(targetAudience -> targetAudience.name().equals(target))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + target + " value!"));
  }

  public static Stream<TargetAudience> stream() {
    return Stream.of(TargetAudience.values());
  }

  public String getValue() {
    return value;
  }
}
