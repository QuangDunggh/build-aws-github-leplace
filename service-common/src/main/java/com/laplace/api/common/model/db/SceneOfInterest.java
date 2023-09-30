package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@IdClass(SceneOfInterestPK.class)
@Table(name = DBTables.SCENE_OF_INTEREST)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SceneOfInterest {

  @Id
  @Column(name = "user_id")
  private Integer userId;

  @Id
  @Column(name = "tag_attribute_id")
  private Integer tagAttributeId;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "name")
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SceneOfInterest that = (SceneOfInterest) o;
    return Objects.equals(userId, that.userId) &&
        Objects.equals(tagAttributeId, that.tagAttributeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tagAttributeId);
  }
}
