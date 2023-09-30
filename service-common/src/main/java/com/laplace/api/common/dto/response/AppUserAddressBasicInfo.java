package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserAddressBasicInfo {

  private String zip;

  private String prefectures;

  private String prefectureKana;

  private String municipalityChome;

  private String municipalityChomeKana;

  private String street;

  private String streetKana;

  private String buildingNameAndRoomNumber;

  private String buildingNameAndRoomNumberKana;

  public static AppUserAddressBasicInfo make(Address address) {

    return address == null ? null : AppUserAddressBasicInfo.builder()
        .zip(address.getZip())
        .prefectures(address.getPrefectures())
        .prefectureKana(address.getPrefectureKana())
        .street(address.getStreet())
        .streetKana(address.getStreetKana())
        .municipalityChomeKana(address.getMunicipalityChomeKana())
        .municipalityChome(address.getMunicipalityChome())
        .buildingNameAndRoomNumberKana(address.getBuildingNameRoomNumberKana())
        .buildingNameAndRoomNumber(address.getBuildingNameRoomNumber())
        .build();
  }
}
