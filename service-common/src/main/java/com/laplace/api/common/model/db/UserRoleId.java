package com.laplace.api.common.model.db;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private AdminUser adminUser;
  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(adminUser).append(role).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof UserRoleId)) {
      return false;
    }
    UserRoleId other = (UserRoleId) obj;
    return new EqualsBuilder().append(this.adminUser, other.adminUser)
        .append(this.role, other.role).isEquals();
  }
}
