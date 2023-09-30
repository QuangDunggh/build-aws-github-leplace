package com.laplace.api.web.service;

import com.laplace.api.common.dto.request.FollowDTO;

public interface WMCFollowService {

  void followUser(Integer id, FollowDTO followDTO);

  FollowDTO isUserFollowed(Integer id, Integer userId);
}
