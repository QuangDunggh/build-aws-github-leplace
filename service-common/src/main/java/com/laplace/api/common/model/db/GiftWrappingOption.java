package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = DBTables.GIFT_WRAPPING_OPTION)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftWrappingOption implements Serializable {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "settings_id", nullable = false, updatable = false)
  private Integer settingsId;

  @Column(name = "gift_wrapping_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private GiftWrappingType giftWrappingType;

  @Column(name = "dimension", nullable = false)
  private String dimension;

  @Column(name = "description")
  private String description;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GiftWrappingOption that = (GiftWrappingOption) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
