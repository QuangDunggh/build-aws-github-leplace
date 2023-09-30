package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.DBTables;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = DBTables.APP_USER_PROFILE)
public class AppUserProfile {

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "first_name_kana")
  private String firstNameKana;

  @Column(name = "last_name_kana")
  private String lastNameKana;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "birth_date")
  private ZonedDateTime birthDate;

  @Column(name = "profile_image")
  private String profileImage;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "address_id")
  private Integer addressId;

  @Column(name = "user_bank_id")
  private Integer userBankId;

  @Column(name = "stripe_customer_id")
  private String customerKey;

  @Column(name = "stripe_account_id")
  private String accountId;

  @Column(name = "created_by", nullable = false)
  private Integer createdBy;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(userId).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AppUserProfile)) {
      return false;
    }
    return new EqualsBuilder().append(this.userId, ((AppUserProfile) obj).userId).isEquals();
  }

  public static AppUserProfile emptyProfile() {
    AppUserProfile profile = new AppUserProfile();
    profile.setFirstName(ApplicationConstants.StringUtils.EMPTY_STRING);
    profile.setLastName(ApplicationConstants.StringUtils.EMPTY_STRING);
    profile.setUserName(ApplicationConstants.StringUtils.EMPTY_STRING);
    profile.setProfileImage(ApplicationConstants.StringUtils.EMPTY_STRING);
    return profile;
  }

  public String getName() {
    return StringUtils.defaultString(getFirstName())
        + StringUtils.SPACE
        + StringUtils.defaultString(getLastName());
  }

  public String getKataKanaName() {
    return StringUtils.defaultString(getFirstNameKana())
        + StringUtils.SPACE
        + StringUtils.defaultString(getLastNameKana());
  }
}
