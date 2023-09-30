package com.laplace.api.common.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDocument {

  @Field(type = FieldType.Keyword, copyTo = {"keywords"})
  private String en;

  @Field(type = FieldType.Keyword, copyTo = {"keywords"})
  private String ja;

}
