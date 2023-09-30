package com.laplace.api.web.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.converter.FavoriteConverter;
import com.laplace.api.common.dto.request.FavoriteDto;
import com.laplace.api.common.model.db.Favorite;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.service.FavoriteService;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.service.WMCFavoriteService;
import com.laplace.api.web.service.WMCItemService;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WMCFavoriteServiceImpl implements WMCFavoriteService {

  private final AuthenticationFacade authenticationFacade;
  private final FavoriteService favoriteService;
  private final FavoriteConverter favoriteConverter;
  private final WMCItemService wmcItemService;

  @Autowired
  WMCFavoriteServiceImpl(
      AuthenticationFacade authenticationFacade,
      FavoriteService favoriteService,
      FavoriteConverter favoriteConverter,
      WMCItemService wmcItemService) {
    this.authenticationFacade = authenticationFacade;
    this.favoriteService = favoriteService;
    this.favoriteConverter = favoriteConverter;
    this.wmcItemService = wmcItemService;
  }

  @Transactional
  @Override
  public void favoriteItem(String itemId, FavoriteDto favoriteDto) {
    Integer userId = authenticationFacade.getUserId();
    Item item = wmcItemService.findItem(itemId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));

    boolean isAlreadyFavorite = favoriteService.isFavoriteByUserIdAndItemId(userId, itemId);
    if (BooleanUtils.isFalse(favoriteDto.getFavorite())) {
      if(isAlreadyFavorite) {
        favoriteService.delete(Favorite.builder()
            .userId(userId)
            .itemId(itemId).build());
        wmcItemService.updateFavCount(itemId, item, false);
      }
    } else {
      if(!isAlreadyFavorite) {
        favoriteService.save(favoriteConverter.make(itemId, userId));
        wmcItemService.updateFavCount(itemId, item, true);
      }
    }

  }
}



