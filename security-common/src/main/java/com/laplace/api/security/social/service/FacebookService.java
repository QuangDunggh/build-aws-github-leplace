package com.laplace.api.security.social.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.helper.RestClient;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.social.config.FacebookConfig;
import com.laplace.api.security.social.service.model.FacebookLongLiveTokenResponse;
import com.laplace.api.security.social.service.model.FacebookVerificationResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FacebookService {

  private final FacebookConfig facebookConfig;
  private final ObjectMapper objectMapper;
  private final RestClient restClient;
  private final Messages messages;

  @Autowired
  public FacebookService(FacebookConfig facebookConfig, ObjectMapper objectMapper,
      Messages messages) {
    this.facebookConfig = facebookConfig;
    this.objectMapper = objectMapper;
    this.restClient = new RestClient(facebookConfig.getBaseUrl());
    this.messages = messages;
  }

  /**
   * Verify facebook access token
   *
   * @param accessToken client access token
   * @return token status
   */
  public TokenStatus verify(String accessToken, String facebookUserId) {
    TokenStatus tokenStatus = new TokenStatus();
    String url = String.format(facebookConfig.getAccessTokenValidationUrl(), accessToken);
    String response = this.restClient.get(url);
    try {
      FacebookVerificationResponse responseObject = objectMapper
          .readValue(response, FacebookVerificationResponse.class);
      if (responseObject.getId() != null && responseObject.getId().equals(facebookUserId)) {
        tokenStatus.setValid(true);
        tokenStatus.setUserId(responseObject.getId());
        tokenStatus.setEmail(responseObject.getEmail());
      } else {
        tokenStatus.setMessage(responseObject.getError().getMessage());
      }

    } catch (IOException | NullPointerException ex) {
      log.error("Facebook verify me failed,", ex);
      tokenStatus.setMessage(messages.getInvalidToken());
    }

    return tokenStatus;
  }

  /**
   * Generate long live token
   *
   * @param accessToken
   * @return
   */
  public FacebookLongLiveTokenResponse generateLongLiveToken(String accessToken) {
    FacebookLongLiveTokenResponse longLiveTokenResponse = null;
    try {
      String url = String.format(facebookConfig.getLongLiveTokenUrl(), facebookConfig.getAppId(),
          facebookConfig.getAppSecret(), accessToken);
      String response = this.restClient.get(url);
      longLiveTokenResponse = objectMapper
          .readValue(response, FacebookLongLiveTokenResponse.class);
    } catch (Exception ex) {
      log.error(
          "Facebook long live token generation failed with { accessToken:" + accessToken + "} ,",
          ex);
    }

    return longLiveTokenResponse;
  }
}
