package com.laplace.api.common.util;

public final class ContentIDGeneration {

  public static String getGoodsId(Integer clientId, Integer goodsId) {
    return String.format("g_%s_%s", clientId, goodsId);
  }

  public static String getCoordinateId(String coordinateId, Integer clientId, Integer type) {
    return String.format("co_%d_%d_%s", clientId, type, coordinateId);
  }
}
