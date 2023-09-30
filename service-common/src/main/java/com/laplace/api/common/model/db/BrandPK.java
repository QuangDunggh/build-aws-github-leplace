package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import lombok.Data;

@Data
public class BrandPK implements Serializable {

  @Column(updatable = false, name = "id")
  private Integer id;

  @Column(updatable = false, name = "client_id")
  private Integer clientId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrandPK brandPK = (BrandPK) o;
    return Objects.equals(id, brandPK.id) &&
        Objects.equals(clientId, brandPK.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientId);
  }
}
