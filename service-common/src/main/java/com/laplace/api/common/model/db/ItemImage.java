package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.DBTables;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = DBTables.ITEM_IMAGE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class ItemImage {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "item_id", nullable = false)
  private String itemId;

  @Column(name = "is_cover_image", nullable = false)
  private boolean isCoverImage;

  @Column(name = "is_receipt_image", nullable = false)
  private boolean isReceiptImage;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "num")
  private Integer number;

  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemImage that = (ItemImage) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
