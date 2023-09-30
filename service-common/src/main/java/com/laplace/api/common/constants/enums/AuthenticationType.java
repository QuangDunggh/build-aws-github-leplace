package com.laplace.api.common.constants.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.laplace.api.common.util.ApplicationContextUtils;
import com.laplace.api.common.util.Messages;

public enum AuthenticationType {
  @JsonProperty("1")
  BASIC(1),
  @JsonProperty("2")
  FACEBOOK(2),
  @JsonProperty("3")
  TWITTER(3),
  ;

  private int type;

  AuthenticationType(int type) {
    this.type = type;
  }

  @JsonCreator
  public static AuthenticationType forValue(Integer value) {
    for (AuthenticationType authenticationType : AuthenticationType.values()) {
      if (authenticationType.type == value) {
        return authenticationType;
      }
    }
    throw new IllegalArgumentException(ApplicationContextUtils.getContext().getBean(Messages.class)
        .getInvalidAuthenticationType());
  }

  @JsonValue
  public int getValue() {
    return type;
  }
}
