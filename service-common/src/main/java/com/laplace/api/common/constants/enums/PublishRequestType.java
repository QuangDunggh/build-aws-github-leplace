package com.laplace.api.common.constants.enums;


import com.laplace.api.common.util.ApplicationContextUtils;
import com.laplace.api.common.util.Messages;
import java.util.stream.Stream;

public enum PublishRequestType {
  COORD_GOODS_PHOTO(1, "PHOTO_GOODS"),
  COORD_ONLY_GOODS(2, "ONLY_GOODS"),
  COORD_ONLY_PHOTO(3, "ONLY_PHOTO"),
  GOODS_TOPS(4, "TOPS"),
  GOODS_BOTTOMS(5, "BOTTOMS"),
  GOODS_ALL_IN_ONE(6, "ALL_IN_ONE"),
  GOODS_OTHERS(7, "OTHERS"),
  MOVIES(8, "MOVIES");

  private int id;
  private String name;

  PublishRequestType(int val, String name) {
    this.id = val;
    this.name = name;
  }

  public static PublishRequestType fromMenuName(String menuName) {
    for (PublishRequestType publishRequestType : PublishRequestType.values()) {
      if (publishRequestType.name.equalsIgnoreCase(menuName)) {
        return publishRequestType;
      }
    }
    throw new IllegalArgumentException(ApplicationContextUtils.getContext().getBean(Messages.class)
        .getInvalidPublishRequestType());
  }

  public static PublishRequestType fromMenuId(int menuId) {
    for (PublishRequestType publishRequestType : PublishRequestType.values()) {
      if (publishRequestType.id == menuId) {
        return publishRequestType;
      }
    }
    throw new IllegalArgumentException(ApplicationContextUtils.getContext().getBean(Messages.class)
        .getInvalidPublishRequestType());
  }

  public static boolean isValidGoodsMenu(String menuName) {
    switch (PublishRequestType.fromMenuName(menuName)) {
      case GOODS_TOPS:
      case GOODS_BOTTOMS:
      case GOODS_ALL_IN_ONE:
      case GOODS_OTHERS:
        return true;
      default:
        return false;
    }
  }

  public static boolean isValidCoordinateMenu(String menuName) {
    switch (PublishRequestType.fromMenuName(menuName)) {
      case COORD_GOODS_PHOTO:
      case COORD_ONLY_GOODS:
      case COORD_ONLY_PHOTO:
        return true;
      default:
        return false;
    }
  }

  public static PublishRequestType getFromGoodsType(GoodsType goodsType) {
    return Stream.of(PublishRequestType.values())
        .filter(prt -> prt.getMenuName().equalsIgnoreCase(goodsType.name()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String
            .format(com.laplace.api.common.constants.Messages.INVALID_ENUM_ARGS, goodsType.getId(),
                CoordinateType.class.getName())));
  }

  public static PublishRequestType getFromCoordinateType(CoordinateType coordinateType) {
    return Stream.of(PublishRequestType.values())
        .filter(prt -> prt.getMenuName().equalsIgnoreCase(coordinateType.name()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String
            .format(com.laplace.api.common.constants.Messages.INVALID_ENUM_ARGS,
                coordinateType.getCoordinateTypeId(),
                CoordinateType.class.getName())));
  }

  public int getId() {
    return id;
  }

  public String getMenuName() {
    return name;
  }
}
