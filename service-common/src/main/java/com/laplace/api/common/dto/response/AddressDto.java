package com.laplace.api.common.dto.response;

import com.laplace.api.common.dto.DeliveryFeeDto;
import com.laplace.api.common.model.db.Address;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

  private Integer id;
  private Integer userId;
  private String zip;
  private String prefectures;
  private String city;
  private String prefectureKana;
  private String municipalityChome;
  private String municipalityChomeKana;
  private String street;
  private String streetKana;
  private String buildingNameRoomNumber;
  private String buildingNameRoomNumberKana;
  private Boolean asDefault;
  private DeliveryFeeDto deliveryInfo;

  public static AddressDto from(Address address) {
    return AddressDto.builder()
        .id(address.getId())
        .userId(address.getUserId())
        .zip(address.getZip())
        .prefectures(address.getPrefectures())
        .prefectureKana(address.getPrefectureKana())
        .city(address.getCity())
        .municipalityChome(address.getMunicipalityChome())
        .municipalityChomeKana(address.getMunicipalityChomeKana())
        .street(address.getStreet())
        .streetKana(address.getStreetKana())
        .buildingNameRoomNumber(address.getBuildingNameRoomNumber())
        .buildingNameRoomNumberKana(address.getBuildingNameRoomNumberKana())
        .asDefault(address.getAsDefault())
        .build();
  }

  public static AddressDto from(Address address, DeliveryFeeDto deliveryFeeDto) {
    AddressDto addressDto = from(address);
    addressDto.setDeliveryInfo(deliveryFeeDto);
    return addressDto;
  }

  public static Address copyProperties(AddressDto addressDto, Address address) {
    address.setId(checkNull(addressDto.getId(), address.getId()));
    address.setUserId(checkNull(addressDto.getUserId(), address.getUserId()));
    address.setZip(checkNull(addressDto.getZip(), address.getZip()));
    address.setPrefectures(checkNull(addressDto.getPrefectures(), address.getPrefectures()));
    address.setCity(checkNull(addressDto.getCity(), address.getCity()));
    address.setPrefectureKana(checkNull(addressDto.getPrefectureKana(), address.getPrefectureKana()));
    address.setMunicipalityChome(
        checkNull(addressDto.getMunicipalityChome(), address.getMunicipalityChome()));
    address.setMunicipalityChomeKana(
        checkNull(addressDto.getMunicipalityChomeKana(), address.getMunicipalityChomeKana()));
    address.setStreet(checkNull(addressDto.getStreet(), address.getStreet()));
    address.setStreetKana(checkNull(addressDto.getStreetKana(), address.getStreetKana()));
    address.setBuildingNameRoomNumber(
        checkNull(addressDto.getBuildingNameRoomNumber(), address.getBuildingNameRoomNumber()));
    address.setBuildingNameRoomNumberKana(checkNull(addressDto.getBuildingNameRoomNumberKana(),
        address.getBuildingNameRoomNumberKana()));
    address.setAsDefault(checkNull(addressDto.getAsDefault(), address.getAsDefault()));
    return address;
  }

  private static <T> T checkNull(T current, T old) {
    return Objects.isNull(current) ? old : current;
  }
}