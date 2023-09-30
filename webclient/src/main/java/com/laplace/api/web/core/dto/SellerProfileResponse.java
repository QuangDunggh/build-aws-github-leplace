package com.laplace.api.web.core.dto;

import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.dto.request.FollowDTO;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.SellerProfile;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerProfileResponse {

  private String userName;

  private String profileImage;

  private String sellClosetImage;

  private String sellClosetName;

  private Boolean shareFb;

  private Boolean shareTwitter;

  private boolean facebookEnable;

  private boolean twitterEnable;

  private String facebookProfileId;

  private String twitterProfileId;

  private Boolean isFollowed;

  public static SellerProfileResponse from(SellerProfile sellerProfile,
      AppUser appUser) {
    return SellerProfileResponse.builder()
        .profileImage(sellerProfile.getProfileImage())
        .userName(appUser.getOrEmptyProfile().getUserName())
        .sellClosetImage(sellerProfile.getSellClosetImage())
        .sellClosetName(sellerProfile.getSellClosetName())
        .shareFb(sellerProfile.getShareFb())
        .shareTwitter(sellerProfile.getShareTwitter())
        .facebookEnable(appUser.isFacebookEnable())
        .twitterEnable(appUser.isTwitterEnable())
        .facebookProfileId(appUser.getFacebookUserId())
        .twitterProfileId(appUser.getTwitterUserId())
        .build();
  }

  public static SellerProfileResponse from(SellerProfile sellerProfile, AppUser appUser,
      FollowDTO followDTO) {
    return Objects.isNull(sellerProfile) ? nullSellerProfile(appUser, followDTO)
        : SellerProfileResponse.builder()
            .profileImage(sellerProfile.getProfileImage())
            .userName(appUser.getOrEmptyProfile().getUserName())
            .sellClosetImage(sellerProfile.getSellClosetImage())
            .sellClosetName(sellerProfile.getSellClosetName())
            .shareFb(sellerProfile.getShareFb())
            .shareTwitter(sellerProfile.getShareTwitter())
            .facebookEnable(appUser.isFacebookEnable())
            .twitterEnable(appUser.isTwitterEnable())
            .facebookProfileId(appUser.getFacebookUserId())
            .twitterProfileId(appUser.getTwitterUserId())
            .isFollowed(followDTO.getFollow())
            .build();
  }

  public static SellerProfileResponse nullSellerProfile(AppUser appUser, FollowDTO followDTO) {
    return SellerProfileResponse.builder()
        .profileImage(StringUtils.EMPTY_STRING)
        .userName(appUser.getOrEmptyProfile().getUserName())
        .sellClosetImage(StringUtils.EMPTY_STRING)
        .sellClosetName(StringUtils.EMPTY_STRING)
        .shareFb(Boolean.FALSE)
        .shareTwitter(Boolean.FALSE)
        .facebookEnable(appUser.isFacebookEnable())
        .twitterEnable(appUser.isTwitterEnable())
        .facebookProfileId(appUser.getFacebookUserId())
        .twitterProfileId(appUser.getTwitterUserId())
        .isFollowed(followDTO.getFollow())
        .build();
  }
}
