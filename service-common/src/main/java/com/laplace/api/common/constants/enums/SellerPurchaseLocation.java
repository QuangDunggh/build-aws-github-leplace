package com.laplace.api.common.constants.enums;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum SellerPurchaseLocation {
  OFFICIAL_STORE_REAL("オフィシャルストア（リアル店舗)"),
  OFFICIAL_STORE_NET("オフィシャルストア（ネット)"),
  STORE_REAL("セレクトショップ（リアル店舗）"),
  STORE_NET("ネットストア"),
  RECYCLE_SHOP("リサイクルショップ"),
  NET_FLEA_MARKET("ネットフリマ"),
  NET_AUCTION("ネットオークション"),
  OTHER("その他");

  private String value;

  SellerPurchaseLocation(String value) {
    this.value = value;
  }

  private static Supplier<Stream<SellerPurchaseLocation>>
  purchaseLocationSupplier() {
    return () -> Stream.of(SellerPurchaseLocation.values());
  }

  public static SellerPurchaseLocation forName(String purchaseLocation) {
    return purchaseLocationSupplier().get()
        .filter(location -> location.name().equals(purchaseLocation))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(
                "Cannot create enum from " + purchaseLocation + " value!"));
  }

  public String getValue() {
    return value;
  }
}
