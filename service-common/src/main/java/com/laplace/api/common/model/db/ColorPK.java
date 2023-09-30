package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorPK implements Serializable {

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
    ColorPK primaryKey = (ColorPK) o;
    return Objects.equals(id, primaryKey.id) &&
        Objects.equals(clientId, primaryKey.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientId);
  }
}
