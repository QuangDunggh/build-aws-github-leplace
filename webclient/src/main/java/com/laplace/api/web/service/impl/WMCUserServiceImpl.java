package com.laplace.api.web.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;
import static java.util.stream.Collectors.toMap;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.request.FollowDTO;
import com.laplace.api.common.dto.response.AppUserBasicInfo;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.SellerProfile;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.SellerProfileService;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.web.core.dto.SellerProfileResponse;
import com.laplace.api.web.service.WMCFollowService;
import com.laplace.api.web.service.WMCUserService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WMCUserServiceImpl implements WMCUserService {

  private final AppUserService appUserService;
  private final SellerProfileService profileService;
  private final WMCFollowService wmcFollowService;

  @Autowired
  public WMCUserServiceImpl(AppUserService appUserService,
      SellerProfileService profileService,
      WMCFollowService wmcFollowService) {
    this.appUserService = appUserService;
    this.profileService = profileService;
    this.wmcFollowService = wmcFollowService;
  }

  @Override
  public PageableResponseDTO<AppUserBasicInfo> findBestSeller(Pageable page) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, "itemsSentToLaplace")));
    Page<AppUser> userByPage = appUserService.findBestSellers(request);
    List<AppUser> userList = userByPage.getContent();
    Map<Integer, SellerProfile> sellerProfileMap = profileService
        .findByIdIn(userList.stream().map(AppUser::getUserId).collect(Collectors.toSet()))
        .stream().collect(toMap(SellerProfile::getSellerId, Function.identity()));
    return PageableResponseDTO.create(userByPage.getTotalElements(), userByPage.getTotalPages(),
        userList.stream().map(user -> AppUserBasicInfo.from(user, sellerProfileMap))
            .collect(Collectors.toList()));
  }

  @Override
  public SellerProfileResponse findSellerProfile(Integer id, Integer userId) {
    SellerProfile sellerProfile = profileService.findById(id).orElse(null);
    AppUser appUser = appUserService.findById(id)
        .orElseThrow(() -> returnApplicationException(ResultCodeConstants.USER_PROFILE_NOT_EXISTS));
    FollowDTO followDTO = wmcFollowService.isUserFollowed(id, userId);

    return SellerProfileResponse.from(sellerProfile, appUser, followDTO);
  }
}
