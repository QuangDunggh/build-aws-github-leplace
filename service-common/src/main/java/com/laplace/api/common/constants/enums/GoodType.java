package com.laplace.api.common.constants.enums;

public enum GoodType {

  TOPS(1),
  BOTTOMS(2),
  ALL_IN_ONE(3),
  OTHERS(4);

  private int type;

  GoodType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
