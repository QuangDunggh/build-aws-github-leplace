package com.laplace.api.security.social.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookLongLiveTokenResponse {

  //{ "access_token" : "{long-lived-user-access-token}" , "token_type" : "bearer" , "expires_in" : 5183944 //Number of seconds until this token expires }
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("expires_in")
  private Integer expiresInSec;
  private FacebookError error;
}
