package com.laplace.api.common.converter;

import com.laplace.api.common.model.db.Follow;
import com.laplace.api.common.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class FollowConverter {

  public Follow make(Integer userId, Integer sellerId) {
    return Follow.builder()
        .userId(userId)
        .sellerId(sellerId)
        .createdOn(DateUtil.timeNow())
        .build();
  }
}
