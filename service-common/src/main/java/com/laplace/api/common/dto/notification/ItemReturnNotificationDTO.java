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
public class ItemReturnNotificationDTO {

  private String brandName;
  private String itemName;
  private String itemId;
  private String image;
  private Integer displayPrice;
  private String paymentListUrl;

  public static String makeJson(ObjectMapper objectMapper, Item item, String paymentListUrl) {
    try {
      return objectMapper.writeValueAsString(ItemReturnNotificationDTO.builder()
          .brandName(item.getBrandName())
          .itemName(item.getItemName())
          .itemId(item.getItemId())
          .image(item.getCoverImage())
          .displayPrice(item.getDisplayPrice())
          .paymentListUrl(paymentListUrl)
          .build());
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }
  }
}
