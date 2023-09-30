package com.laplace.api.common.service;

import com.laplace.api.common.model.db.Favorite;
import java.util.Map;
import java.util.Set;

public interface FavoriteService {

  boolean isFavoriteByUserIdAndItemId(Integer userId, String itemId);

  void delete(Favorite favorite);

  Favorite save(Favorite favorite);

  Map<String, Boolean> findFavoritesByUserId(Integer userId, Set<String> collect);
}
