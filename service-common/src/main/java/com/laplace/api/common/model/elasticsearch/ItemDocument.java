package com.laplace.api.common.model.elasticsearch;

import com.laplace.api.common.constants.SearchConstants;
import com.laplace.api.common.constants.SearchConstants.Type;
import java.util.Objects;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = SearchConstants.Index.ITEMS,createIndex = false)
@TypeAlias(Type.ITEMS)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDocument {

  @Id
  @Field(type = FieldType.Keyword)
  private String id;

  @Field(type = FieldType.Integer)
  private Integer sellerId;

  @Field(type = FieldType.Text, copyTo = {"keywords"})
  private String sellerEmail;

  @Field(type = FieldType.Text, copyTo = {"keywords"})
  private String itemName;

  @Field(type = FieldType.Text, copyTo = {"keywords"})
  private String descriptions;

  @Field(type = FieldType.Keyword)
  private String status;

  @Field(type = FieldType.Keyword)
  private String targetAudience;

  @Field(type = FieldType.Keyword)
  private String category;

  @Field(type = FieldType.Object, includeInParent = true)
  private CategoryDocument subCategory;

  @Field(type = FieldType.Integer)
  private Integer brandId;

  @Field(type = FieldType.Text, copyTo = {"keywords"})
  private String brandName;

  @Field(type = FieldType.Text)
  private String coverImage;

  @Field(type = FieldType.Boolean)
  private Boolean receiptAvailable;

  @Field(type = FieldType.Text)
  private String receiptImage;

  @Field(type = FieldType.Long)
  private Long displayRequestDate;

  @Field(type = FieldType.Long)
  private Long publishedAt;

  @Field(type = FieldType.Keyword)
  private String sellerPurchaseTime;

  @Field(type = FieldType.Keyword)
  private Long estimatedPickUpTimeByLaplace;

  @Field(type = FieldType.Text)
  private String color;

  @Field(type = FieldType.Text)
  private String size;

  @Field(type = FieldType.Text)
  private String dimensions;

  @Field(type = FieldType.Integer)
  private Integer displayPrice;

  @Field(type = FieldType.Integer)
  private Integer discountPercentage;

  @Field(type = FieldType.Integer)
  private Integer prevDisplayPrice;

  @Field(type = FieldType.Keyword)
  private String deliveryFeeBearer;

  @Field(type = FieldType.Keyword)
  private String physicalCondition;

  @Field(type = FieldType.Keyword)
  private String sellerPurchaseLocation;

  @Field(type = FieldType.Keyword)
  private String judgementStatus;

  @Field(type = FieldType.Boolean)
  private Boolean pickUp;

  @Field(type = FieldType.Long)
  private Long pickUpAt;

  @Field(type = FieldType.Boolean)
  private Boolean hidden;

  @Field(type = FieldType.Long)
  private Long favoriteCount;

  @Field(type = FieldType.Boolean)
  private Boolean blacklisted;

  @Field(type = FieldType.Integer)
  private Integer createdBy;

  @Field(type = FieldType.Long)
  private Long createdOn;

  @Field(type = FieldType.Integer)
  private Integer lastUpdatedBy;

  @Field(type = FieldType.Long)
  private Long lastUpdatedOn;

  @Field(type = FieldType.Long)
  private Long onHoldAt;

  @Field(type = FieldType.Text, analyzer = SearchConstants.Analyzer.KUROMOJI,
      searchAnalyzer = SearchConstants.Analyzer.KUROMOJI)
  private String keywords;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemDocument that = (ItemDocument) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}
