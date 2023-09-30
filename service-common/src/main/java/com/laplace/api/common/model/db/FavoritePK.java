package com.laplace.api.common.model.db;


import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class FavoritePK implements Serializable {

  private Integer userId;
  private String itemId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FavoritePK that = (FavoritePK) o;
    return Objects.equals(getUserId(), that.getUserId()) &&
        Objects.equals(getItemId(), that.getItemId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getItemId());
  }
}
