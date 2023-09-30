package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = DBTables.USERS_ROLES)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {

  @EmbeddedId
  private UserRoleId id;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof UserRole)) {
      return false;
    }
    UserRole other = (UserRole) obj;
    return new EqualsBuilder().append(this.id, other.id).isEquals();
  }
}
