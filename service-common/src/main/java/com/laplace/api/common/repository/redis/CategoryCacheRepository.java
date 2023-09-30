package com.laplace.api.common.repository.redis;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryCacheRepository {

  private final ListOperations<String, String> listOperations;

  @Autowired
  public CategoryCacheRepository(RedisTemplate<String, String> valueStringTemplate) {
    this.listOperations = valueStringTemplate.opsForList();
  }

  public List<String> getSubCategories(String key, Integer start, Integer end) {
    return listOperations.range(key, start, end);
  }

}
