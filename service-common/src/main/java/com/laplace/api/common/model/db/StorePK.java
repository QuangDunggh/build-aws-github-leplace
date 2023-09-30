package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorePK implements Serializable {

  private Integer id;
  private Integer clientId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StorePK primaryKey = (StorePK) o;
    return Objects.equals(clientId, primaryKey.clientId) &&
        Objects.equals(id, primaryKey.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientId, id);
  }
}