package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class FollowPK  implements Serializable {

  private Integer userId;
  private Integer sellerId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FollowPK that = (FollowPK) o;
    return Objects.equals(getUserId(), that.getUserId()) &&
        Objects.equals(getSellerId(), that.getSellerId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getSellerId());
  }
}
