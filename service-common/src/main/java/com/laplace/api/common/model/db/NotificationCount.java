package com.laplace.api.common.model.db;


import com.laplace.api.common.constants.DBTables;
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
@Table(name = DBTables.NOTIFICATION_COUNT)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationCount {

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "advertise_count")
  @Builder.Default
  private Integer advertiseCount = 0;

  @Column(name = "borrow_count")
  @Builder.Default
  private Integer borrowCount = 0;

  @Column(name = "accept_reject_count")
  @Builder.Default
  private Integer acceptRejectCount = 0;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationCount that = (NotificationCount) o;
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }
}
