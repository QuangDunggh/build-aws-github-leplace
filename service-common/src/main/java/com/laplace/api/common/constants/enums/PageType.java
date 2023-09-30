package com.laplace.api.common.constants.enums;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.INVALID_PAGE_TYPE;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum PageType {
  HOME(1, false),
  GOOD_DETAILS(2, false),
  COORDINATE_DETAILS(3, false),
  MOVIE_DETAILS(4, false),
  TERMS_OF_SERVICE(5, false),
  PRIVACY_POLICY(6, false),
  FAQ(7, false),
  SPECIFIED_COMMERICAL_TRANSACTION_LAW(8, false),
  COPYRIGHT_INFORMATION(9, false),
  HELP(10, false),
  LANDING_PAGE(11, true),
  CLUSTER_LANDING_PAGE(12, true),
  OCCASION_LANDING_PAGE(13, true),
  NEWS(14, false),
  REVIEW(15, false),
  STYLIST(16, false);

  private int type;
  private boolean isMultiple;

  PageType(int type, boolean isMultiple) {
    this.type = type;
    this.isMultiple = isMultiple;
  }

  private static Supplier<Stream<PageType>> pageTypesSupplier() {
    return () -> Stream.of(PageType.values());
  }

  public static PageType getPageTypeFromVal(int val) {
    return pageTypesSupplier().get()
        .filter(pageType -> pageType.getType() == val)
        .findFirst().orElseThrow(() -> throwApplicationException(INVALID_PAGE_TYPE));
  }

  public int getType() {
    return type;
  }

  public boolean isMultiple() {
    return isMultiple;
  }
}
