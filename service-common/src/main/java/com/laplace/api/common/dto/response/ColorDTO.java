package com.laplace.api.common.dto.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColorDTO implements Serializable {

  @Field(type = FieldType.Integer)
  private Integer id;
  @Field(type = FieldType.Keyword)
  private String name;
  @Field(type = FieldType.Keyword)
  private String code;
}
