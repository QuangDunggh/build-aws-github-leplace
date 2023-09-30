package com.laplace.api.cms.core.bean;

import com.laplace.api.common.constants.ErrorCode;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequest {

  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer clientId;

  @NotEmpty(message = ErrorCode.INVALID_ARGUMENT)
  private List<Attribute> list;

  @Data
  @AllArgsConstructor
  public static class Attribute {

    @NotNull
    private Integer id;
    private boolean isConnected;
  }
}
