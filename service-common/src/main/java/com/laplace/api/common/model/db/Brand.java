package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = DBTables.BRANDS)
@Data
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "brand_id", nullable = false, updatable = false)
  private Integer brandId;

  @Column(name = "brand_name", nullable = false, unique = true)
  private String brandName;

  @Column(name = "brand_name_jp")
  private String brandNameJp;

  @Column(name = "brand_image")
  private String image;

  @Column(name = "is_popular", columnDefinition = "BIT(1) DEFAULT '0'")
  private Boolean isPopular;

  @Column(name = "last_popular_at")
  private ZonedDateTime lastPopularAt;

  @Column(name = "is_visible")
  private Boolean isVisible;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  public static Brand of(Integer brandId) {
    Brand brand = new Brand();
    brand.setBrandId(brandId);
    return brand;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return new EqualsBuilder().append(this.getBrandId(), ((Brand) obj).getBrandId()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(brandId).build();
  }
}
