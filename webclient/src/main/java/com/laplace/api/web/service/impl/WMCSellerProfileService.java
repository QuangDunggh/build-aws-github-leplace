package com.laplace.api.web.service.impl;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.SellerProfile;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.SellerProfileService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.core.bean.SellerProfileUpdateRequest;
import com.laplace.api.web.core.dto.SellerProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Service
public class WMCSellerProfileService {

  private final SellerProfileService profileService;
  private final AuthenticationFacade authenticationFacade;
  private final AppUserService appUserService;

  @Autowired
  public WMCSellerProfileService(SellerProfileService profileService,
      AuthenticationFacade authenticationFacade,
      AppUserService appUserService) {
    this.profileService = profileService;
    this.authenticationFacade = authenticationFacade;
    this.appUserService = appUserService;
  }

  public SellerProfileResponse update(SellerProfileUpdateRequest request) {
    Integer userId = authenticationFacade.getUserId();
    AppUser appUser = appUserService.findById(userId)
        .filter(user -> !ObjectUtils
            .nullSafeEquals(user.getVerificationStatus(), VerificationStatus.NOT_VERIFIED))
        .filter(user -> isTrue(user.getProfileComplete()))
        .orElseThrow(() -> returnApplicationException(ResultCodeConstants.USER_PROFILE_NOT_EXISTS));

    if ((isTrue(request.getShareFb()) && isFalse(appUser.isFacebookEnable()))
        || (isTrue(request.getShareTwitter()) && isFalse(appUser.isTwitterEnable()))) {
      throwApplicationException(ResultCodeConstants.UNAUTHORIZED_OPERATION);
    }
    SellerProfile sellerProfile = profileService.findById(userId)
        .orElse(defaultProfile(userId));
    profileService.save(addProperties(sellerProfile, request));

    appUser.getAppUserProfile().setUserName(request.getUserName());
    appUserService.saveUser(appUser);
    return SellerProfileResponse.from(sellerProfile, appUser);
  }

  public SellerProfileResponse getProfile() {
    Integer userId = authenticationFacade.getUserId();
    SellerProfile sellerProfile = profileService.findById(userId)
        .orElse(defaultProfile(userId));
    AppUser appUser = appUserService.findById(userId)
        .orElseThrow(() -> returnApplicationException(ResultCodeConstants.USER_PROFILE_NOT_EXISTS));

    return SellerProfileResponse.from(sellerProfile, appUser);
  }

  private SellerProfile defaultProfile(Integer userId) {
    return SellerProfile.builder()
        .sellerId(userId)
        .createdOn(DateUtil.timeNow())
        .build();
  }

  private SellerProfile addProperties(SellerProfile profile, SellerProfileUpdateRequest request) {
    profile.setProfileImage(request.getProfileImage());
    profile.setSellClosetImage(request.getSellClosetImage());
    profile.setSellClosetName(request.getSellClosetName());
    profile.setShareFb(isTrue(request.getShareFb()));
    profile.setShareTwitter(isTrue(request.getShareTwitter()));
    profile.setLastUpdatedBy(profile.getSellerId());
    profile.setLastUpdatedOn(DateUtil.timeNow());
    return profile;
  }
}
