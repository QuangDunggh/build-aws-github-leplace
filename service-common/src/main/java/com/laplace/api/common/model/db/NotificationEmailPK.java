package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class NotificationEmailPK implements Serializable {

  @Column(name = "user_id")
  protected Integer userId;
  protected String emailAddress;

  public NotificationEmailPK() {

  }

  public NotificationEmailPK(Integer userId, String emailAddress) {
    this.userId = userId;
    this.emailAddress = emailAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NotificationEmailPK)) {
      return false;
    }
    NotificationEmailPK that = (NotificationEmailPK) o;
    return Objects.equals(getUserId(), that.getUserId()) &&
        Objects.equals(getEmailAddress(), that.getEmailAddress());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getEmailAddress());
  }
}
