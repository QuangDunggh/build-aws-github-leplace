package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.UserBasicInfoResponseDto;
import com.laplace.api.common.model.db.AppUserProfile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserInfoConverter implements Converter<AppUserProfile, UserBasicInfoResponseDto> {

  @Override
  public UserBasicInfoResponseDto convert(AppUserProfile source) {
    return UserBasicInfoResponseDto.builder()
        .id(source.getUserId())
        .firstName(source.getFirstName())
        .familyName(source.getLastName())
        .profileImage(source.getProfileImage())
        .build();
  }
}
