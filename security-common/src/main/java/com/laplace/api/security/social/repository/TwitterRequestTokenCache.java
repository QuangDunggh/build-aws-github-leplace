package com.laplace.api.security.social.repository;

import com.laplace.api.common.constants.ApplicationConstants;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import twitter4j.auth.RequestToken;

@Repository
@Slf4j
public class TwitterRequestTokenCache {

  private static final String PREFIX = "twitter-";
  private static final long EVICTION_TIME_MIN = 5;
  private final RedisTemplate<String, RequestToken> redisTemplate;
  private final ValueOperations<String, RequestToken> valueOperations;

  public TwitterRequestTokenCache(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = this.redisTemplate.opsForValue();
  }

  public void putToken(RequestToken requestToken) {
    valueOperations.set(getKey(requestToken.getToken()), requestToken);
    redisTemplate.expire(getKey(requestToken.getToken()), EVICTION_TIME_MIN, TimeUnit.MINUTES);
  }

  public Optional<RequestToken> getToken(String tokenKey) {
    return Optional.ofNullable(valueOperations.get(getKey(tokenKey)));
  }

  public void evictToken(String tokenKey) {
    redisTemplate.delete(getKey(tokenKey));
  }

  private String getKey(String tokenKey) {
    return PREFIX + ApplicationConstants.StringUtils.COLON + tokenKey;

  }
}
