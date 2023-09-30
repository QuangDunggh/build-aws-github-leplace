package com.laplace.api.common.model.redis;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {

  private Integer id;
  private Integer clientId;
  private Integer largeClassificationId;
  private Integer smallClassificationId;

  @Override
  public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
    Goods that = (Goods) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(clientId, that.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, clientId);
  }
}
