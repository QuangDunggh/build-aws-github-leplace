package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Category {
  CLOTHING("ウェア"),
  SHOES("シューズ"),
  BAGS("バッグ"),
  ACCESSORIES("小物"),
  JEWELRY("ジュエリー"),
  INTERIOR("インテリア");

  private String value;

  Category(String value) {
    this.value = value;
  }

  private static Supplier<Stream<Category>>
  categorySupplier() {
    return () -> Stream.of(Category.values());
  }

  public static Category forName(String category) {
    return categorySupplier().get()
        .filter(condition -> condition.name().equals(category))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + category + " value!"));
  }

  public static Stream<Category> stream() {
    return Stream.of(Category.values());
  }

  public String getValue() {
    return value;
  }
}
