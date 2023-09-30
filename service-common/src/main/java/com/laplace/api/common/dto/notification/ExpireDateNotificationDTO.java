package com.laplace.api.common.dto.notification;

import lombok.Data;

@Data
public class ExpireDateNotificationDTO {

  private String brandName;
  private String itemName;
  private String itemId;
  private String image;
  private String displayPrice;
  private String status;
}
