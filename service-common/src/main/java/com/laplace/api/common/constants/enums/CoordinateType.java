package com.laplace.api.common.constants.enums;

import com.laplace.api.common.constants.Messages;
import java.util.stream.Stream;

public enum CoordinateType {
  ONLY_GOODS(1),
  ONLY_PHOTO(2),
  PHOTO_GOODS(3);

  private final Integer id;

  CoordinateType(Integer id) {
    this.id = id;
  }

  public static CoordinateType fromId(Integer id) {
    return Stream.of(CoordinateType.values()).filter(coordinateType -> coordinateType.id == id)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, id,
                CoordinateType.class.getName())));
  }

  public int getCoordinateTypeId() {
    return id;
  }
}
