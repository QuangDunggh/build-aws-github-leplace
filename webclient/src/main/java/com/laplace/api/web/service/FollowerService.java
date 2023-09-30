package com.laplace.api.web.service;

import com.laplace.api.common.constants.enums.FollowerCriteria;
import com.laplace.api.common.dto.response.FollowerResponseDTO;

public interface FollowerService {

  FollowerResponseDTO getFollowers(Integer userId, FollowerCriteria criteria);
}
