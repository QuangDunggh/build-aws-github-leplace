package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.laplace.api.common.constants.Messages;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum SearchSortType {
  RECENT(1),
  PRICE_LOW_TO_HIGH(2),
  PRICE_HIGH_TO_LOW(3),
  POPULAR(4),
  LISTING_DATE_DESC(5),
  LISTING_DATE_ASC(6),
  PICK_UP_DATE_DESC(7),
  PICK_UP_DATE_ASC(8),
  DISPLAY_DATE_DESC(9),
  DISPLAY_DATE_ASC(10);

  private int value;

  SearchSortType(int value) {
    this.value = value;
  }

  @JsonCreator
  public static SearchSortType forValue(int forValue) {
    return supplier().get().filter(sort -> sort.getValue() == forValue)
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, forValue,
                CSVType.class.getName())));
  }

  private static Supplier<Stream<SearchSortType>> supplier() {
    return () -> Stream.of(SearchSortType.values());
  }

  @JsonValue
  public int getValue() {
    return value;
  }
}
