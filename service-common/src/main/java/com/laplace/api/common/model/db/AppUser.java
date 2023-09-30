package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

@Entity
@Getter
@Setter
@Table(name = DBTables.APP_USERS)
@DynamicUpdate
public class AppUser implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, updatable = false)
  private Integer userId;

  @Column(name = "email", unique = true)
  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "fb_user_id", unique = true)
  private String facebookUserId;

  @Column(name = "twitter_user_id", unique = true)
  private String twitterUserId;

  @Column(name = "fb_enable", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private boolean facebookEnable;

  @Column(name = "twitter_enable", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private boolean twitterEnable;

  @Column(name = "active", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private boolean active;

  @Column(name = "access_id")
  private String accessId;

  @Column(name = "items_sent_to_laplace")
  private Long itemsSentToLaplace;

  @Column(name = "items_on_display")
  private Long itemsOnDisplay;

  @Column(name = "items_sold")
  private Long itemsSold;

  @Column(name = "user_status", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL DEFAULT NORMAL")
  @Enumerated(EnumType.STRING)
  private UserStatus userStatus;

  @Column(name = "verification_status", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL DEFAULT NOT_VERIFIED")
  @Enumerated(EnumType.STRING)
  private VerificationStatus verificationStatus;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "user_id")
  private Set<Address> addresses;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "user_id")
  private AppUserProfile appUserProfile;

  @Column(name = "profile_complete", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private Boolean profileComplete;

  @Column(name = "search_keyword")
  private String searchKeyword;

  @Column(name = "account_withdrawn", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private boolean accountWithdrawn;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private AccountWithdrawReason accountWithdrawReason;

  @Column(name = "unread_count", nullable = false, columnDefinition = "DEFAULT 0")
  private Long unreadCount;

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
    if (!(obj instanceof AppUser)) {
      return false;
    }
    return new EqualsBuilder().append(this.userId, ((AppUser) obj).userId).isEquals();
  }

  public AppUserProfile getOrEmptyProfile() {
    return ObjectUtils.isEmpty(getAppUserProfile()) ? AppUserProfile.emptyProfile() : getAppUserProfile();
  }

  public String getFullName() {
    AppUserProfile profile = getOrEmptyProfile();
    return (profile.getFirstName() + StringUtils.SPACE + profile.getLastName()).trim();
  }

  public String getSurnameOrderedName() {
    AppUserProfile profile = getOrEmptyProfile();
    return (profile.getLastName() + StringUtils.SPACE + profile.getFirstName()).trim();
  }
}
