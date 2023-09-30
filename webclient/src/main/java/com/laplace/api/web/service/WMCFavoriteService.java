package com.laplace.api.web.service;

import com.laplace.api.common.dto.request.FavoriteDto;

public interface WMCFavoriteService {

  void favoriteItem(String id, FavoriteDto favoriteDto);
}
