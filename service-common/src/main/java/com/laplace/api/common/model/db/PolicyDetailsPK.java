package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.ErrorCode;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDetailsPK implements Serializable {

  @Column(name = "policy_id")
  @NotNull(message = ErrorCode.POLICY_ID_NOT_FOUND)
  private Integer policyId;

  @Column(name = "policy_priority", updatable = false)
  private Integer policyPriority;

  @Column(name = "period")
  @NotNull(message = ErrorCode.PERIOD_NOT_FOUND)
  private String period;

  @Column(name = "logic_id")
  @NotNull(message = ErrorCode.LOGIC_ID_NOT_FOUND)
  private Integer logicId;

  @Column(name = "policy_type_id")
  @NotNull(message = ErrorCode.POLICY_TYPE_ID_NOT_FOUND)
  private Integer policyTypeId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PolicyDetailsPK that = (PolicyDetailsPK) o;

    return new EqualsBuilder()
        .append(policyId, that.policyId)
        .append(period, that.period)
        .append(logicId, that.logicId)
        .append(policyTypeId, that.policyTypeId)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(policyId)
        .append(period)
        .append(logicId)
        .append(policyTypeId)
        .toHashCode();
  }
}
