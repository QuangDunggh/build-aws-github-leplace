package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Table(name = DBTables.ADMIN_USERS, indexes = {
    @Index(columnList = "email", name = "Uk_email", unique = true)
})
@DynamicUpdate
public class AdminUser implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, updatable = false)
  private Integer userId;

  @Column(name = "email")
  @Email(regexp = ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX, message = "Please provide a valid email")
  @NotEmpty
  private String email;

  @Column(name = "password")
  @NotEmpty
  private String password;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @Column(name = "active", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private boolean active;

  @Column(name = "access_id")
  private String accessId;

  @OneToMany(mappedBy = "id.adminUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<UserRole> userRoles = new LinkedHashSet<>();

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "user",
      fetch = FetchType.LAZY, optional = false)
  private CMSUserProfileModel profile;

  public void addRole(Role role) {
    UserRole userRole = new UserRole(new UserRoleId(this, role));
    userRoles.add(userRole);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(userId).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AdminUser)) {
      return false;
    }
    return new EqualsBuilder().append(this.userId, ((AdminUser) obj).userId).isEquals();
  }

}
