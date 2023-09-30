package com.laplace.api.common.model.db;


import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
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
@IdClass(FavoritePK.class)
@Table(name = DBTables.FAVORITE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

  @Id
  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Id
  @Column(name = "item_id", nullable = false)
  private String itemId;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Favorite that = (Favorite) o;
    return Objects.equals(getUserId(), that.getUserId()) &&
        Objects.equals(getItemId(), that.getItemId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getItemId());
  }
}
