package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.NoticeStatus;
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
@Table(name = DBTables.ADMIN_NOTICE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminNotice {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "title")
  private String title;

  @Column(name = "message")
  private String message;

  @Column(name = "text_of_link")
  private String textOfLink;

  @Column(name = "link")
  private String link;

  @Column(name = "image")
  private String image;

  @Column(name = "notice_status")
  @Enumerated(EnumType.STRING)
  private NoticeStatus noticeStatus;

  @Column(name = "send_time")
  private ZonedDateTime sendTime;

  @Column(name = "send_email")
  private Boolean sendEmail;

  @Column(name = "created_on")
  private ZonedDateTime createdOn;

  @Column(name = "updated_on")
  private ZonedDateTime updatedOn;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminNotice that = (AdminNotice) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
