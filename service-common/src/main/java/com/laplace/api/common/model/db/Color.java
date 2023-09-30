package com.laplace.api.common.model.db;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "colors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color implements Serializable {

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @EmbeddedId
  private ColorPK colorPK;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Color that = (Color) o;
    return Objects.equals(colorPK.getId(), that.getColorPK().getId()) &&
        Objects.equals(colorPK.getClientId(), that.getColorPK().getClientId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(colorPK.getId(), colorPK.getClientId());
  }
}
