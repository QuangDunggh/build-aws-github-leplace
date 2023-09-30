package com.laplace.api.common.service;

import com.laplace.api.common.dto.response.AppUserProfileResponseDto;
import com.laplace.api.common.model.db.AppUserProfile;
import java.util.Optional;

public interface UserInfoService {

  Optional<AppUserProfile> findById(Integer userId);

  AppUserProfileResponseDto getUserProfileDetails(Integer userId);
}
