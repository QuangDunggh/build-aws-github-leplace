package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.RemindPeriod;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = DBTables.INITIAL_SETTINGS)
@Data
public class InitialSettings implements Serializable {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "processing_rate")
  private Integer processingRate;

  @Column(name = "delivery_fee")
  private Integer deliveryFee;

  @Column(name = "delivery_fee_hokkaido")
  private Integer deliveryFeeHokkaido;

  @Column(name = "delivery_fee_okinawa")
  private Integer deliveryFeeOkinawa;

  @Column(name = "delivery_fee_island")
  private Integer deliveryFeeIsland;

  @Column(name = "cancel_fee")
  private Integer cancelFee;

  @Column(name = "cancel_delivery_fee")
  private Integer cancelDeliveryFee;

  @Column(name = "cancel_delivery_fee_hokkaido")
  private Integer cancelDeliveryFeeHokkaido;

  @Column(name = "cancel_delivery_fee_okinawa")
  private Integer cancelDeliveryFeeOkinawa;

  @Column(name = "cancel_delivery_fee_island")
  private Integer cancelDeliveryFeeIsland;

  @Column(name = "display_extension_fee")
  private Integer displayExtensionFee;

  @Column(name = "display_period")
  private Integer displayPeriod;

  @Enumerated(EnumType.STRING)
  @Column(name = "remind_period")
  private RemindPeriod remindPeriod;

  @Column(name = "gift_wrapping_fee")
  private Integer giftWrappingFee;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "settingsId", orphanRemoval = true)
  private Set<GiftWrappingOption> giftWrappingOptions;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return new EqualsBuilder().append(this.getId(), ((InitialSettings) obj).getId()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(id).build();
  }
}
