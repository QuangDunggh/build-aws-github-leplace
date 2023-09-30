package com.laplace.api.common.constants.enums;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import java.util.Arrays;

public enum PageSection {
  EYE_CATCH_IMAGE("Eye catch image"),
  INTRO_DESC("Description for introduction"),
  MOVIE_DESC("Description for movie section"),
  COORD_DESC("Description for coordinate section"),
  GOODS_DESC("Description for goods section"),
  H1_TAG("H1 tag"),
  SERVICE_DESCRIPTION("Service description"),
  RENTAL_IMAGE("Rental image"),
  PURCHASE_IMAGE("Purchase image"),
  RENTAL_DESC("Rental description"),
  PURCHASE_DESC("Purchase description"),
  NEWS("News"),
  STYLIST("Stylist"),
  GOODS("Goods"),
  STYLIST_REVIEW_DESC("Stylist review description"),
  NEWS_DESC("News description"),
  RELATED_COORD_DESC("Related coordinate description"),
  RELATED_GOODS_DESC("Related goods description"),
  TERMS_OF_SERVICE("Terms of service"),
  PRIVACY_POLICY("Privacy policy"),
  FAQ("FAQ"),
  SPECIFIED_COMMERICAL_TRANSACTION_LAW("Specified commercial transaction law"),
  COPYRIGHT_INFORMATION("Copyright information"),
  HELP("Help"),
  CLUSTER_LP_IMAGE_URL("Cluster lp image url"),
  CLUSTER_LP_DESC("Cluster lp description"),
  OCCASION_LP_IMAGE_URL("Occasion lp image url"),
  OCCASION_LP_DESC("Occasion lp description"),
  AFFILIATE_LP_TAGS("Affiliate lp tags");

  private String sectionName;

  PageSection(String sectionName) {
    this.sectionName = sectionName;
  }

  public static PageSection fromSectionName(String sectionName) {
    return Arrays.stream(PageSection.values())
        .filter(pageSection -> pageSection.getSectionName().equalsIgnoreCase(sectionName))
        .findFirst()
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.INVALID_PAGE_SECTION));
  }

  public String getSectionName() {
    return sectionName;
  }
}
