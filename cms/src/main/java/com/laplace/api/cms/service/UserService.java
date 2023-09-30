package com.laplace.api.cms.service;

import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.dto.response.AppUserBasicInfo;
import com.laplace.api.common.dto.response.AppUserProfileResponseDto;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {

  AppUserProfileResponseDto getAppUserProfile(Integer userId);

  PageableResponseDTO<AppUserBasicInfo> getAppUserList(String keyword,
      List<VerificationStatus> verificationStatus, Pageable pageable);

  void changeUserStatus(Integer userId);

  void changeVerificationStatus(Integer userId, VerificationStatus verificationStatus);
}
