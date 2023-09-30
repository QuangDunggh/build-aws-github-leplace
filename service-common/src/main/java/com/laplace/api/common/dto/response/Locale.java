package com.laplace.api.common.dto.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Locale implements Serializable {

  private String language;

  private String value;

}
