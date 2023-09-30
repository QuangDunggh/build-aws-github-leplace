package com.laplace.api.common.constants.enums;

import com.laplace.api.common.constants.Messages;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum StripeParamsType {

  EMAIL("email"), DESCRIPTION("description"), CARD("card"), DEFAULT_CARD("default_card"),
  AMOUNT("amount"), CURRENCY("currency"), METADATA("metadata"), CAPTURE("capture"),
  SOURCE("source"), CHARGE("charge"), CUSTOMER("customer"), CURRENCY_JP("jpy"),
  TYPE("type"), CLIENT_SECRET("clientSecret"), CODE("code"), STATUS("status"),
  COUNTRY("JP"), MCC("5691"), URL("https://leclair.co.jp"),
  PRODUCT_DESCRIPTIONS("Clothes, shoes, accessories, jewelery and other purchase platform");

  private String value;

  StripeParamsType(String value) {
    this.value = value;
  }

  /**
   * Get pay jp params from name. Validate params name
   *
   * @param value name
   * @return PayJPParamsType
   */
  public static StripeParamsType fromName(String value) {
    return Stream.of(StripeParamsType.values()).filter(type -> type.value == value)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, value,
                CoordinateType.class.getName())));
  }
}