package com.laplace.api.common.constants.enums;

import java.util.HashMap;
import java.util.Map;

public enum LocaleType {
  UNDEFINED(-1),
  JAPANESE(1),
  ENGLISH(2),
  CHINESE(3);

  private static final Map<Integer, LocaleType> lookup = new HashMap<>();

  static {
    for (LocaleType d : LocaleType.values()) {
      lookup.put(d.getValue(), d);
    }
  }

  private int value;

  LocaleType(int value) {
    this.value = value;
  }

  public static LocaleType forValue(int value) {
    return lookup.getOrDefault(value, UNDEFINED);
  }

  public int getValue() {
    return value;
  }
}
