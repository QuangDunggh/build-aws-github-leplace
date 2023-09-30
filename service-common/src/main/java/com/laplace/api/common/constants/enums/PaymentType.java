package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

public enum PaymentType {
  PAY(1),
  REFUND(2),
  FIX(3),
  RELEASE(4);

  private static final Map<Integer, PaymentType> lookup = new HashMap<>();

  static {
    for (PaymentType d : PaymentType.values()) {
      lookup.put(d.getValue(), d);
    }
  }

  private int type;

  PaymentType(int type) {
    this.type = type;
  }

  @JsonCreator
  public static PaymentType forValue(int value) {
    return lookup.get(value);
  }

  @JsonValue
  public int getValue() {
    return type;
  }
}
