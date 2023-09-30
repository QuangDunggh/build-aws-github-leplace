package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.laplace.api.common.constants.MetaTagRanks;
import java.util.HashMap;
import java.util.Map;

public enum DefaultMetaTags {

  TITLE("title", MetaTagRanks.TITLE),
  DESCRIPTION("description", MetaTagRanks.DESCRIPTION),
  KEYWORDS("keywords", MetaTagRanks.KEYWORDS),
  OG_DESCRIPTION("og:description", MetaTagRanks.OG_DESCRIPTION),
  OG_TYPE("og:type", MetaTagRanks.OG_TYPE),
  OG_URL("og:url", MetaTagRanks.OG_URL),
  OG_IMAGE("og:image", MetaTagRanks.OG_IMAGE);

  private static final Map<String, DefaultMetaTags> lookup = new HashMap<>();

  static {
    for (DefaultMetaTags d : DefaultMetaTags.values()) {
      lookup.put(d.getTagName(), d);
    }
  }

  private String type;
  private int rating;
  DefaultMetaTags(String type, int rating) {
    this.type = type;
    this.rating = rating;
  }

  @JsonCreator
  public static DefaultMetaTags forTagName(String value) {
    return lookup.get(value);
  }

  /**
   * Check if a given tag is default
   *
   * @param val
   * @return
   */
  public static boolean isDefaultTag(String val) {
    return lookup.values()
        .stream()
        .anyMatch(defaultMetaTags -> defaultMetaTags.getTagName().equalsIgnoreCase(val));
  }

  @JsonValue
  public String getTagName() {
    return type;
  }

  public int getTagRating() {
    return rating;
  }
}
