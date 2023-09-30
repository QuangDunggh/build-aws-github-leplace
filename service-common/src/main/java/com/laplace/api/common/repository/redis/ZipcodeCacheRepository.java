package com.laplace.api.common.repository.redis;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ZipcodeCacheRepository {

  private final SetOperations<String, String> setOperations;
  private final String ZIPCODE_CACHE_PREFIX = "zipcode:";

  @Autowired
  public ZipcodeCacheRepository(RedisTemplate<String, String> valueStringTemplate) {
    this.setOperations = valueStringTemplate.opsForSet();
  }

  public Boolean isZipCodeExist(String region, String zipCode) {
    return setOperations.isMember(ZIPCODE_CACHE_PREFIX + region, zipCode);
  }
}
