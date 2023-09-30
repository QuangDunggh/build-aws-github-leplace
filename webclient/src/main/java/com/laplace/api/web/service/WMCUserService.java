package com.laplace.api.web.service;

import com.laplace.api.common.dto.response.AppUserBasicInfo;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.web.core.dto.SellerProfileResponse;
import org.springframework.data.domain.Pageable;

public interface WMCUserService {

  PageableResponseDTO<AppUserBasicInfo> findBestSeller(Pageable page);

  SellerProfileResponse findSellerProfile(Integer id, Integer userId);
}
