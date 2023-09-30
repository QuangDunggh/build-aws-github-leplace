package com.laplace.api.cms.core.dto;

import java.util.List;
import lombok.Data;

@Data
public class NotificationSettingsDto {

  private Boolean notificationSetting;
  private List<String> emails;
}
