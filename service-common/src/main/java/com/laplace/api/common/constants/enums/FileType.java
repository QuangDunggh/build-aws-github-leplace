package com.laplace.api.common.constants.enums;

public enum FileType {
  COVER_IMAGE("cover"),
  PROFILE_IMAGE("profile"),
  LOGO_IMAGE("logo_image"),
  FAV_ICON_IMAGE("fav_icon_image"),
  SECTION_IMAGE("section_image"),
  ITEM_IMAGE("item_image"),
  ITEM_RECEIPT_IMAGE("item_receipt_image"),
  BRAND_IMAGE("brand_image"),
  VIDEO("video"),
  PUBLIC_RESOURCE("public_resource");

  private String fileType;

  FileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileType() {
    return fileType;
  }
}
