package com.laplace.api.web.core.enums;

import com.laplace.api.common.constants.Messages;
import com.laplace.api.common.constants.enums.CoordinateType;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum PayJPParamsType {

  EMAIL("email"), DESCRIPTION("description"), CARD("card"), DEFAULT_CARD("default_card"),
  AMOUNT("amount"), CURRENCY("currency"), METADATA("metadata"), CAPTURE("capture"),
  SOURCE("source"), EXPIRY_DAYS("expiry_days");

  private String value;

  PayJPParamsType(String value) {
    this.value = value;
  }

  /**
   * Get pay jp params from name. Validate params name
   *
   * @param value name
   * @return PayJPParamsType
   */
  public static PayJPParamsType fromName(String value) {
    return Stream.of(PayJPParamsType.values()).filter(type -> type.value == value)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, value,
                CoordinateType.class.getName())));
  }
}
