package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum ItemStatus {
  WAITING_FOR_ARRIVAL("到着待ち"),
  ITEM_INSPECTION("検品中"),
  DEFINE_DISPLAY_DATE("販売条件設定中"),
  DISPLAY_DATE_RESERVED("販売日時予約済み"),
  ON_SALE("販売中"),
  PREPARE_TO_SEND_TO_BUYER("配送準備中"),
  ON_THE_WAY_TO_BUYER("配送手配済み"),
  TRANSACTION_COMPLETE("取引完了"),
  PREPARE_TO_SEND_TO_SELLER("取引完了"),
  ON_THE_WAY_TO_SELLER("返品手配済み"),
  DISPLAY_CANCELLED("出品キャンセル済み"),
  ON_HOLD("出品一時停止"),
  ON_HOLD_PERIOD_EXCEEDED("一時停止期間超過"),
  ON_SALE_OVER_SIX_MONTH_EXCEEDED("販売中（6ヶ月超過)");

  private String value;

  ItemStatus(String value) {
    this.value = value;
  }

  private static Supplier<Stream<ItemStatus>>
  itemStatusSupplier() {
    return () -> Stream.of(ItemStatus.values());
  }

  public static ItemStatus forName(String status) {
    return itemStatusSupplier().get()
        .filter(itemStatus -> itemStatus.name().equals(status))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + status + " value!"));
  }

  public String getValue() {
    return value;
  }
}
