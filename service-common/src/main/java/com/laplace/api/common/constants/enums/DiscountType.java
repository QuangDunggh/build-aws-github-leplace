package com.laplace.api.common.constants.enums;

public enum DiscountType {
  PERCENTAGE(1),
  PRICE(2);

  private int type;

  DiscountType(int type) {
    this.type = type;
  }

  public int getType() {
    return this.type;
  }
}
