package com.laplace.api.web.controller;

import com.laplace.api.common.dto.request.FavoriteDto;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCFavoriteService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class FavoriteController {

  private final WMCFavoriteService wmcFavoriteService;

  @Autowired
  FavoriteController(WMCFavoriteService wmcFavoriteService) {
    this.wmcFavoriteService = wmcFavoriteService;
  }

  @PutMapping(APIEndPoints.FAVORITE)
  public BaseResponse favoriteItem(@PathVariable("id") String id,
      @Valid @RequestBody FavoriteDto favoriteDto) {
    wmcFavoriteService.favoriteItem(id, favoriteDto);
    return BaseResponse.create();
  }

}
