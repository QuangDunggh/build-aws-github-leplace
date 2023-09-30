package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DBTables.SELLER_PROFILE)
public class SellerProfile {

  @Id
  @Column(name = "seller_id")
  private Integer sellerId;

  @Column(name = "profile_image")
  private String profileImage;

  @Column(name = "sell_closet_image")
  private String sellClosetImage;

  @Column(name = "sell_closet_name")
  private String sellClosetName;

  @Column(name = "share_fb", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private Boolean shareFb;

  @Column(name = "share_twitter", columnDefinition = "BIT(1) NOT NULL DEFAULT '0'")
  private Boolean shareTwitter;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getSellerId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SellerProfile)) {
      return false;
    }
    return Objects.equals(this.getSellerId(), ((SellerProfile) obj).getSellerId());
  }

}
