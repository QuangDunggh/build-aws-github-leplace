package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.ErrorCode;
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Data
@Table(name = DBTables.POLICY)
public class Policy implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "policy_id", nullable = false, updatable = false)
  private Integer policyId;

  @Column(name = "summary")
  @NotNull(message = ErrorCode.SUMMARY_NOT_FOUND)
  private String summary;

  @Column(name = "category_id")
  @NotNull(message = ErrorCode.CATEGORY_ID_NOT_FOUND)
  private Integer categoryId;

  @Column(name = "category_name")
  @NotNull(message = ErrorCode.CATEGORY_NAME_NOT_FOUND)
  private String categoryName;

  @Column(name = "client_id")
  @NotNull(message = ErrorCode.CLIENT_ID_NOT_FOUND)
  private Integer clientId;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "policy_id", insertable = false, updatable = false, referencedColumnName = "policy_id")
  private Set<PolicyDetails> policyDetails = new LinkedHashSet<>();

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(policyId).build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Policy)) {
      return false;
    }
    return new EqualsBuilder().append(this.policyId, ((Policy) obj).policyId).isEquals();
  }
}
