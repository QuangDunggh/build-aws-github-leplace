package com.laplace.api.common.constants.enums;

import java.util.stream.Stream;

public enum SupportedImageType {
  JPG("jpg"),
  JPEG("jpeg"),
  PNG("png"),
  GIF("gif"),
  ICO("vnd.microsoft.icon");

  private String imageType;

  SupportedImageType(String imageType) {
    this.imageType = imageType;
  }

  public static SupportedImageType forImageType(String imageType) {
    return Stream.of(SupportedImageType.values())
        .filter(supportedImageType -> supportedImageType.imageType.equalsIgnoreCase(imageType))
        .findFirst()
        .orElse(null);
  }
}
