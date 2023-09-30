package com.laplace.api.security.social.service;

import com.datastax.driver.core.utils.UUIDs;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.util.LaplaceResponseUtil;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.model.TwitterAccessTokenDTO;
import com.laplace.api.security.social.config.TwitterConfig;
import com.laplace.api.security.social.repository.TwitterRequestTokenCache;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

@Service
@Slf4j
public class TwitterService {

  private final TwitterConfig twitterConfig;
  private final Messages messages;
  private final TwitterRequestTokenCache requestTokenCache;
  private final TwitterFactory twitterFactory;

  @Autowired
  public TwitterService(TwitterConfig twitterConfig, Messages messages,
      TwitterRequestTokenCache requestTokenCache) {
    this.twitterConfig = twitterConfig;
    this.messages = messages;
    this.requestTokenCache = requestTokenCache;
    twitterFactory = this.getTwitterFactory();
  }

  /**
   * Verify twitter client access token & acess token secret and return token status
   *
   * @param accessToken       twitter access token
   * @param accessTokenSecret twitter access token secret
   * @return token status.
   */
  public TokenStatus verify(String accessToken, String accessTokenSecret) {

    TokenStatus accessStatus = new TokenStatus();
    if (accessToken != null && accessTokenSecret != null) {
      ConfigurationBuilder builder = getConfigurationBuilder();
      builder.setOAuthAccessToken(accessToken);
      builder.setOAuthAccessTokenSecret(accessTokenSecret);
      Twitter twitter = new TwitterFactory(builder.build()).getInstance();
      try {
        User user = twitter.verifyCredentials();
        accessStatus.setValid(true);
        accessStatus.setUserId(String.valueOf(user.getId()));
        accessStatus.setEmail(user.getEmail());
      } catch (TwitterException e) {
        log.error(
            "TwitterException." + e.getErrorMessage() + ".ExceptionCode=" + e.getExceptionCode(),
            e);
        accessStatus.setMessage(messages.getInvalidToken());
      }
    } else {
      accessStatus.setMessage(messages.getInvalidToken());
    }
    return accessStatus;
  }

  public RequestToken getRequestToken(String payLoad, String signUpToken) throws TwitterException {
    StringBuilder callbackUrlBuilder = new StringBuilder(
        !StringUtils.hasText(signUpToken) ? twitterConfig.getLoginCallbackUrl() :
            twitterConfig.getSignUpCallbackUrl());
    if (StringUtils.hasText(payLoad)) {
      callbackUrlBuilder.append("?t=").append(UUIDs.timeBased().timestamp());
    }
    if (StringUtils.hasText(signUpToken)) {
      callbackUrlBuilder.append("?st=").append(signUpToken);
    }
    Twitter twitter = twitterFactory.getInstance();
    RequestToken requestToken = twitter.getOAuthRequestToken(callbackUrlBuilder.toString());
    requestTokenCache.putToken(requestToken);
    return requestToken;
  }

  private ConfigurationBuilder getConfigurationBuilder() {
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.setOAuthConsumerKey(twitterConfig.getConsumerApiKey());
    builder.setOAuthConsumerSecret(twitterConfig.getConsumerApiSecret());
    builder.setIncludeEmailEnabled(true);
    return builder;
  }

  private TwitterFactory getTwitterFactory() {
    return new TwitterFactory(getConfigurationBuilder().build());
  }

  public TwitterAccessTokenDTO getAccessToken(String oauthToken, String oauthVerifier) {
    return requestTokenCache.getToken(oauthToken)
        .flatMap(requestToken -> {
          TwitterAccessTokenDTO tokenDTO = null;
          try {
            Twitter twitter = twitterFactory.getInstance();
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            twitter = twitterFactory.getInstance(accessToken);
            User user = twitter.verifyCredentials();
            tokenDTO = TwitterAccessTokenDTO.builder()
                .accessToken(accessToken.getToken())
                .accessTokenSecret(accessToken.getTokenSecret())
                .email(user.getEmail())
                .twitterId(String.valueOf(accessToken.getUserId()))
                .build();
          } catch (TwitterException e) {
            log.error(e.getErrorMessage(), e);
          }
          return Optional.ofNullable(tokenDTO);
        })
        .orElseThrow(
            () -> LaplaceResponseUtil
                .returnApplicationException(ResultCodeConstants.INVALID_TOKEN));

  }
}


