package com.laplace.api.common.constants.enums;

import com.laplace.api.common.constants.Messages;
import java.util.stream.Stream;

public enum GoodsType {
  TOPS(1),
  BOTTOMS(2),
  ALL_IN_ONE(3),
  OTHERS(4);

  private int id;

  GoodsType(int id) {
    this.id = id;
  }

  public static GoodsType fromId(int id) {
    return Stream.of(GoodsType.values())
        .filter(goodsType -> id == goodsType.id)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, id,
                CoordinateType.class.getName())));
  }

  public int getId() {
    return id;
  }
}
