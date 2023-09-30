package com.laplace.api.cms.core.bean;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class NotificationSettingDto {

  @NotEmpty
  private Boolean notificationSetting;
}
