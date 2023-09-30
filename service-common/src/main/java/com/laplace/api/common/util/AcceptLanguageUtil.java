package com.laplace.api.common.util;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.Languages;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public final class AcceptLanguageUtil {

  private AcceptLanguageUtil() {

  }

  public static String getLanguage() {
    String lang = MDC.get(ApplicationConstants.LANGUAGE);
    if (!StringUtils.hasText(lang)) {
      lang = Languages.JAPANESE;
    }
    return lang;
  }
}
