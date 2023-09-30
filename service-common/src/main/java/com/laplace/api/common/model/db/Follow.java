package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(FollowPK.class)
@Table(name = DBTables.FOLLOW)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

  @Id
  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Id
  @Column(name = "seller_id", nullable = false)
  private Integer sellerId;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Follow that = (Follow) o;
    return Objects.equals(getUserId(), that.getUserId()) &&
        Objects.equals(getSellerId(), that.getSellerId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getSellerId());
  }
}
