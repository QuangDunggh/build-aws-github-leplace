package com.laplace.api.common.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryJpDto {

  List<String> ウェア;
  List<String> シューズ;
  List<String> バッグ;
  List<String> 小物;
  List<String> ジュエリー;
  List<String> その他;
}
