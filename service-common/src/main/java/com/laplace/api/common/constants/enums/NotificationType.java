package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
  NEW_FOLLOWER("new.follower"),
  NEW_NEGOTIATION("new.negotiation"),
  REMINDING_EXPIRED_DATE("reminding.expired.date"),
  ITEM_SOLD("item.sold"),
  DISCOUNT_NOTIFY("discount.notify"),
  ADMIN_NOTICE("admin.notice"),
  ITEM_RETURN("item.return");

  private final String templatePrefix;

  NotificationType(String templatePrefix) {
    this.templatePrefix = templatePrefix;
  }

  @JsonCreator
  public String getValue() {
    return name();
  }

  public String getTemplatePrefix() {
    return templatePrefix;
  }

  public String getTitleTemplate() {
    return templatePrefix + ".title";
  }

  public String getMessageTemplate() {
    return templatePrefix + ".message";
  }
}