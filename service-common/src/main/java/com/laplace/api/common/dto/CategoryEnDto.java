package com.laplace.api.common.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryEnDto {

  List<String> clothing;
  List<String> shoes;
  List<String> bags;
  List<String> accessories;
  List<String> jewelry;
  List<String> other;
}
