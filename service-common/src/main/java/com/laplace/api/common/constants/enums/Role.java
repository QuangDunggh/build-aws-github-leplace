package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.laplace.api.common.constants.Messages;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum Role {
  ADMIN(1),
  SUPER_ADMIN(2),
  UBER_ADMIN(3),
  USER(4),
  SELLER(5);

  private final int value;

  Role(int value) {
    this.value = value;
  }

  /**
   * Returns Role for given value
   *
   * @param value value of Role
   * @return Role representing given value;
   */
  @JsonCreator
  public static Role forValue(int value) {
    return rolesSupplier().get().filter(role -> role.getValue() == value)
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, value,
                Role.class.getName())));
  }

  private static Supplier<Stream<Role>> rolesSupplier() {
    return () -> Stream.of(Role.values());
  }

  public int getValue() {
    return value;
  }
}
