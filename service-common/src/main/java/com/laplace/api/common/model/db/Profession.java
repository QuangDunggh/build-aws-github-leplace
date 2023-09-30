package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Getter
@Setter
@Table(name = DBTables.PROFESSIONS)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profession implements Serializable {

  @Id
  @Column(name = "profession_id")
  private Integer professionId;

  @Column(name = "name")
  private String name;

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(professionId).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Profession)) {
      return false;
    }
    return new EqualsBuilder().append(this.professionId, ((Profession) obj).professionId)
        .isEquals();
  }
}
