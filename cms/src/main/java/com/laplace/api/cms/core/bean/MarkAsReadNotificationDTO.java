package com.laplace.api.cms.core.bean;

import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarkAsReadNotificationDTO {

  @NotNull
  private Set<Integer> ids;
}
