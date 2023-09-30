package com.laplace.api.common.repository.redis;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
public class FavoriteCacheRepository {

  private final SetOperations<String, String> setOperations;
  private final String FAVORITE_CACHE_PREFIX = "favorites-by-user:";

  @Autowired
  public FavoriteCacheRepository(RedisTemplate<String, String> valueStringTemplate) {
    this.setOperations = valueStringTemplate.opsForSet();
  }

  public void addFavorites(Integer userId, Set<String> itemIds) {
    if (ObjectUtils.isEmpty(itemIds)) {
      return;
    }
    setOperations.add(FAVORITE_CACHE_PREFIX + userId, itemIds.toArray(String[]::new));
  }

  public void removeFavorites(Integer userId, String itemId) {
    setOperations.remove(FAVORITE_CACHE_PREFIX + userId, itemId);
  }


  public Set<String> getFavorites(Integer userId) {
    return setOperations.members(FAVORITE_CACHE_PREFIX + userId);
  }
}
