package com.laplace.api.common.service.impl;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.model.db.Favorite;
import com.laplace.api.common.repository.db.FavoriteRepository;
import com.laplace.api.common.repository.redis.FavoriteCacheRepository;
import com.laplace.api.common.service.FavoriteService;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final FavoriteCacheRepository favoriteCacheRepository;

  @Autowired
  FavoriteServiceImpl(FavoriteRepository favoriteRepository,
      FavoriteCacheRepository favoriteCacheRepository) {
    this.favoriteRepository = favoriteRepository;
    this.favoriteCacheRepository = favoriteCacheRepository;
  }

  @Override
  public boolean isFavoriteByUserIdAndItemId(Integer userId, String itemId) {
    Set<String> favorites = findByUserId(userId);
    return favorites.contains(itemId);
  }

  @Override
  public void delete(Favorite favorite) {
    favoriteCacheRepository.removeFavorites(favorite.getUserId(), favorite.getItemId());
    favoriteRepository.delete(favorite);
  }

  @Override
  public Favorite save(Favorite favorite) {
    favoriteCacheRepository.addFavorites(favorite.getUserId(), Collections.singleton(favorite.getItemId()));
    return favoriteRepository.save(favorite);
  }

  @Override
  public Map<String, Boolean> findFavoritesByUserId(Integer userId, Set<String> itemIds) {
    Set<String> favoriteItemIds = findByUserId(userId);
    return itemIds.stream()
        .collect(Collectors.toMap(Function.identity(), favoriteItemIds::contains));
  }

  private Set<String> findByUserId(Integer userId) {

    if (userId.equals(ApplicationConstants.ANONYMOUS_USER)) {
      return Collections.emptySet();
    }
    Set<String> favorites = favoriteCacheRepository.getFavorites(userId);
    if (ObjectUtils.isEmpty(favorites)) {
      favorites = favoriteRepository.findByUserIdOrderByCreatedOnDesc(userId).stream()
          .map(Favorite::getItemId).collect(Collectors.toSet());
      if (!ObjectUtils.isEmpty(favorites)) {
        favoriteCacheRepository.addFavorites(userId, favorites);
      }
    }
    return favorites;
  }
}
