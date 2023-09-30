package com.laplace.api.common.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = DBTables.ROLES)
@Data
public class Role implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "role_id")
  @JsonIgnore
  @NotNull
  private Integer roleId;

  @Column(name = "role_name")
  @NotEmpty
  private String roleName;

  @OneToMany(mappedBy = "id.role", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<UserRole> userRoles = new LinkedHashSet<>();

  public void addUserRole(UserRole userRole) {
    userRoles.add(userRole);
  }

  public void removeUserRole(UserRole userRole) {
    userRoles.remove(userRole);
  }

  @Override
  public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Role)) {
			return false;
		}
    return roleId != null && roleId.equals(((Role) o).getRoleId());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(roleId).toHashCode();
  }
}
