package com.laplace.api.cms.converter;

import com.laplace.api.cms.core.bean.AdminUserProfileDto;
import com.laplace.api.common.model.db.CMSUserProfileModel;
import javax.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdminUserProfileConverter implements
    Converter<AdminUserProfileDto, CMSUserProfileModel> {

  @Override
  public @Nonnull
  CMSUserProfileModel convert(@Nonnull AdminUserProfileDto profileDto) {
    return CMSUserProfileModel.builder()
        .userId(profileDto.getUserId())
        .name(profileDto.getName())
        .faviconImageUrl(profileDto.getFavIconImageUrl())
        .profileImageUrl(profileDto.getProfileImageUrl())
        .build();
  }
}
