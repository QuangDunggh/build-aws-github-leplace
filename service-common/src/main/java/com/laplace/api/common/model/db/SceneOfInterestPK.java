package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class SceneOfInterestPK implements Serializable {

  private Integer userId;
  private Integer tagAttributeId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SceneOfInterestPK that = (SceneOfInterestPK) o;
    return Objects.equals(userId, that.userId) &&
        Objects.equals(tagAttributeId, that.tagAttributeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tagAttributeId);
  }
}
