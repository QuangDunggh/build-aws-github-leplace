package com.laplace.api.web.core.enums;

import com.laplace.api.common.constants.Messages;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum InquiryType {

  ABOUT_PURCHASE(1, "購入について"),
  ABOUT_LISTING(2, "出品について"),
  ABOUT_THE_ACCOUNT(3, "アカウントについて"),
  CANCELLATION(4, "キャンセル・返金について"),
  OTHER_INQUIRIES(5, "その他");

  private Integer id;
  private String value;

  InquiryType(Integer id, String value) {
    this.id = id;
    this.value = value;
  }

  /**
   * Validate Inquiry type id
   *
   * @param id
   * @return
   */
  public static InquiryType fromId(Integer id) {
    return Stream.of(InquiryType.values()).filter(type -> type.id == id)
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS, id,
                InquiryType.class.getName())));
  }
}
