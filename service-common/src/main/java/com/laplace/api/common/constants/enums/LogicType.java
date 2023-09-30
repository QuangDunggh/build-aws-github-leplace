package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum LogicType {
  GOODS_FEE(0, "% of rental goods fee"),
  TAX(1, "% of rental fee including tax"),
  DELIVERY_FEE(2, "% of rental fee including delivery fee"),
  TAX_AND_DELIVERY(3, "% of rental fee including tax & delivery fee");

  private int id;
  private String name;


  LogicType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Returns LogicType for given label name
   *
   * @param labelName name of the logic name
   * @return LogicType representing given label name;
   */
  public static LogicType forName(String labelName) {
    return logicTypeSupplier().get().filter(logicType -> logicType.name.equals(labelName))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Cannot create enum from " + labelName + " value!"));
  }

  private static Supplier<Stream<LogicType>> logicTypeSupplier() {
    return () -> Stream.of(LogicType.values());
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
