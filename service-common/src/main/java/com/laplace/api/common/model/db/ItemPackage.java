package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DBTables.ITEM_PACKAGE)
@Data
public class ItemPackage {

  @Id
  @Column(name = "package_id", nullable = false)
  private String packageId;

  @Column(name = "user_id")
  @NotNull
  private Integer userId;

  @Column(name = "address_id")
  @NotNull
  private Integer addressId;

  @Column(name = "shipping_date")
  private Long estimatedShippingDate ;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return new EqualsBuilder().append(this.getPackageId(), ((ItemPackage) obj).getPackageId()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(packageId).build();
  }
}
