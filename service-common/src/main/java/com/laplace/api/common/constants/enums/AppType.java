package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum AppType {
  WEB_CLIENT(1),
  CMS(2);

  private static final Map<Integer, AppType> lookup = new HashMap<>();

  static {
    for (AppType d : AppType.values()) {
      lookup.put(d.getValue(), d);
    }
  }

  private int type;

  AppType(int type) {
    this.type = type;
  }

  @JsonCreator
  public static AppType forValue(int value) {
    return lookup.get(value);
  }

  static Set<Integer> getValues() {
    Set<Integer> values = new HashSet<>();
    for (AppType t : AppType.values()) {
      values.add(t.getValue());
    }
    return values;
  }

  @JsonValue
  public int getValue() {
    return type;
  }
}
