package com.laplace.api.security.social.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerificationData {

  @JsonProperty("app_id")
  String appId;
  @JsonProperty("application")
  String application;
  @JsonProperty("user_id")
  String userId;
  @JsonProperty("error")
  FacebookError error;

}
