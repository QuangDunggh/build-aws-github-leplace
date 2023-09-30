package com.laplace.api.common.helper.service;

import com.laplace.api.common.constants.enums.PageType;

public final class ClientPageServiceHelper {

  private ClientPageServiceHelper() {
  }

  public static boolean isPageTypeValid(PageType pageType) {
    switch (pageType) {
      case FAQ:
      case HELP:
      case PRIVACY_POLICY:
      case COPYRIGHT_INFORMATION:
      case TERMS_OF_SERVICE:
      case SPECIFIED_COMMERICAL_TRANSACTION_LAW:
        return true;
      default:
        return false;
    }
  }
}
