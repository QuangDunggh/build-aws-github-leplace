package com.laplace.api.common.dto.notification;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.model.db.Item;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class ItemSoldNotificationDTO {

  private String image;
  private String soldItemListUrl;
  private String paymentListUrl;
  private String userName;
  private String itemName;
  private Integer currentPrice;

  public static String makeJson(ObjectMapper objectMapper, Item item,
      String userName, String soldItemListUrl, String paymentListUrl) {
    try {
      return objectMapper.writeValueAsString(ItemSoldNotificationDTO.builder()
          .itemName(item.getItemName())
          .image(item.getCoverImage())
          .soldItemListUrl(soldItemListUrl)
          .paymentListUrl(paymentListUrl)
          .userName(userName)
          .currentPrice(item.getDisplayPrice())
          .build());
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }
  }
}
