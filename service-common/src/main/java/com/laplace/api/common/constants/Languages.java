package com.laplace.api.common.constants;

import com.laplace.api.common.constants.enums.LocaleType;
import java.util.Arrays;
import java.util.List;

public class Languages {

  public static final String JAPANESE = "ja";
  public static final String ENGLISH = "en";
  public static final List<String> APP_USER_MAIL_LANGS = Arrays.asList("ja", "en");
  private static final String DEFAULT_LOCALE_DATA_FOR_UNDEFIENED_LOCALE = ""; // send empty string
  private Languages() {
  }

  public static LocaleType getLocaleTypeFromLanguage(String lang) {
    switch (lang) {
      case JAPANESE:
        return LocaleType.JAPANESE;
      case ENGLISH:
        return LocaleType.ENGLISH;
      default:
        return LocaleType.UNDEFINED;
    }
  }
}
