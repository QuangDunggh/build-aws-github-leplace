package com.laplace.api.common.converter;

import com.laplace.api.common.model.db.Favorite;
import com.laplace.api.common.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class FavoriteConverter {

  public Favorite make(String itemId, Integer userId) {
  return Favorite.builder()
      .itemId(itemId)
      .userId(userId)
      .createdOn(DateUtil.timeNow())
      .build();
  }
}
