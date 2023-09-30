package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.AddressPurpose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity(name = DBTables.ADDRESSES)
@Getter
@Setter
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "zip")
  private String zip;

  @Column(name = "prefectures")
  private String prefectures;

  @Column(name = "prefecture_kana")
  private String prefectureKana;

  @Column(name = "municipality_chome")
  private String municipalityChome;

  @Column(name = "municipality_chome_kana")
  private String municipalityChomeKana;

  @Column(name = "street")
  private String street;

  @Column(name = "city")
  private String city;

  @Column(name = "street_kana")
  private String streetKana;

  @Column(name = "building_name_room_number")
  private String buildingNameRoomNumber;

  @Column(name = "building_name_room_number_kana")
  private String buildingNameRoomNumberKana;

  @Column(name = "deleted")
  private Boolean deleted;

  @Column(name = "address_purpose")
  @Enumerated(EnumType.STRING)
  private AddressPurpose addressPurpose ;

  @Column(name = "as_default")
  private Boolean asDefault;

  @Column(name = "created_by", nullable = false)
  private Integer createdBy;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;
}
