package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.NotificationType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = DBTables.NOTIFICATION)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification implements Serializable {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "from_user_id")
  private Integer fromUserId;

  @Column(name = "title")
  private String title;

  @Column(name = "data_of_message")
  private String dataOfMessage;

  @Column(name = "item_id")
  private String itemId;

  @Column(name = "read_status")
  @Builder.Default
  private boolean readStatus = false;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @Column(name = "created_by")
  private Integer createdBy;

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    Notification that = (Notification) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
