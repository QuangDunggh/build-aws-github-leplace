package com.laplace.api.security.model;

import lombok.Builder;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Builder
public class TwitterAccessTokenDTO {

  private String accessToken;
  private String accessTokenSecret;
  private String email;
  @JsonIgnore
  private String twitterId;
  @JsonIgnore
  private Integer userId;
  private boolean exist;
  private boolean authenticate;
}
