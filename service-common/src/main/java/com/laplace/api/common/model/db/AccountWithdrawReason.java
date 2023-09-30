package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.WithdrawalReason;
import com.laplace.api.common.converter.ReasonToStringConverter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
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
@Table(name = DBTables.ACCOUNT_WITHDRAW_REASONS)
public class AccountWithdrawReason {

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "reason")
  @Convert(converter = ReasonToStringConverter.class)
  private List<WithdrawalReason> reason;

  @Column(name = "detail_reason")
  private String detailReason;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getUserId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AccountWithdrawReason)) {
      return false;
    }
    return Objects.equals(this.getUserId(), ((AccountWithdrawReason) obj).getUserId());
  }

}
