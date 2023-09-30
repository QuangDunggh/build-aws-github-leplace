package com.laplace.api.common.service;

import com.laplace.api.common.model.db.Follow;
import java.util.List;
import java.util.Optional;

public interface FollowService {

  Optional<Follow> findByUserIdAndFollowedSellerId(Integer userId, Integer sellerId);

  List<Follow> findAllByUserIdOrFollowedSellerId(Integer userId, Integer sellerId);

  List<Follow> findAllByUserId(Integer userId);

  List<Follow> findAllBySellerId(Integer sellerId);

  void delete(Follow follow);

  Follow save(Follow follow);
}
