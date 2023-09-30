package com.laplace.api.common.dto;

import com.laplace.api.common.model.db.AppUserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserProfileDto {

  private String firstName;
  private String lastName;
  private String firstNameKana;
  private String lastNameKana;
  private ZonedDateTime dateOfBirth;
  private String phoneNumber;
  private String profileImage;

  public static AppUserProfileDto from(AppUserProfile appUserProfile) {
    return AppUserProfileDto.builder()
        .firstName(appUserProfile.getFirstName())
        .lastName(appUserProfile.getLastName())
        .firstNameKana(appUserProfile.getFirstNameKana())
        .lastNameKana(appUserProfile.getLastNameKana())
        .dateOfBirth(appUserProfile.getBirthDate())
        .phoneNumber(appUserProfile.getPhoneNumber())
        .profileImage(appUserProfile.getProfileImage())
        .build();
  }
}
