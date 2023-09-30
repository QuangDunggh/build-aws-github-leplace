package com.laplace.api.common.model.db;


import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.enums.MeasurementType;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(BodyMeasurementPK.class)
@Table(name = DBTables.BODY_MEASUREMENT)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyMeasurement {

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Id
  @Column(name = "body_part")
  @Enumerated(EnumType.STRING)
  private MeasurementType bodyPart;

  @Column(name = "value")
  private Double value;

  @Column(name = "unit")
  private String unit;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BodyMeasurement that = (BodyMeasurement) o;
    return Objects.equals(userId, that.userId) &&
        Objects.equals(bodyPart, that.bodyPart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, bodyPart);
  }
}
