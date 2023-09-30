package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = DBTables.NOTIFICATION_EMAIL)
public class NotificationEmailModel implements Serializable {

  @EmbeddedId
  private NotificationEmailPK notificationEmailPK;

  @Column(name = "time_stamp")
  private Date timeStamp;

}