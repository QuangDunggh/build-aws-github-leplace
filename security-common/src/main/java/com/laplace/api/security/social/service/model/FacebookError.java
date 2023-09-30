package com.laplace.api.security.social.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookError {

  @JsonProperty("message")
  String message;
  @JsonProperty("type")
  String type;
  @JsonProperty("code")
  Integer code;
}
