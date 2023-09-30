package com.laplace.api.common.model.db;


import com.laplace.api.common.constants.enums.MeasurementType;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class BodyMeasurementPK implements Serializable {

  private Integer userId;
  private MeasurementType bodyPart;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BodyMeasurementPK that = (BodyMeasurementPK) o;
    return Objects.equals(userId, that.userId) &&
        Objects.equals(bodyPart, that.bodyPart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, bodyPart);
  }
}
