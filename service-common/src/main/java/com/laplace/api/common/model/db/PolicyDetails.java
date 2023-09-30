package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.ErrorCode;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Getter
@Setter
@Table(name = DBTables.POLICY_DETAILS)
public class PolicyDetails implements Serializable {

  @EmbeddedId
  private PolicyDetailsPK policyDetailsPK;

  @Column(name = "logic_value")
  @NotNull(message = ErrorCode.LOGIC_VALUE_NOT_FOUND)
  private Integer logicValue;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(policyDetailsPK).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PolicyDetails)) {
      return false;
    }
    return new EqualsBuilder().append(this.policyDetailsPK, ((PolicyDetails) obj).policyDetailsPK)
        .isEquals();
  }
}
