package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.laplace.api.common.constants.Messages;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum CSVType {
  PACKAGE(1),
  AFFILIATE(2),
  HOTEL(3);

  private int type;

  CSVType(int type) {
    this.type = type;
  }

  /**
   * Returns CSVType for given value
   *
   * @param value value of CSVType
   * @return CSVType representing given value;
   */
  @JsonCreator
  public static CSVType forValue(int value) {
    return csvTypesSupplier().get().filter(csvType -> csvType.getValue() == value)
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, value,
                CSVType.class.getName())));
  }

  private static Supplier<Stream<CSVType>> csvTypesSupplier() {
    return () -> Stream.of(CSVType.values());
  }

  @JsonValue
  public int getValue() {
    return type;
  }
}
