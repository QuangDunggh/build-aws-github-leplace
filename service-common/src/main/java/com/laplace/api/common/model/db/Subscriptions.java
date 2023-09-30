package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = DBTables.SUBSCRIPTIONS)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscriptions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private Integer id;

  @Column(name = "settings_id", nullable = false, updatable = false)
  private Integer settingsId;

  @Column(name = "subscription_name", nullable = false)
  private String subscriptionName;

  @Column(name = "subscription_fee", nullable = false)
  private Integer subscriptionFee;

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
    Subscriptions that = (Subscriptions) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
