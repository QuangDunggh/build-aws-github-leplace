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
public class DiscountNotificationDTO {

  private String image;
  private String itemUrl;
  private String itemName;
  private Integer discountPrice;

  public static String makeJson(ObjectMapper objectMapper, Item item, Integer discountPrice,
      String itemUrl) {
    try {
      return objectMapper.writeValueAsString(DiscountNotificationDTO.builder()
          .image(item.getCoverImage())
          .itemUrl(itemUrl)
          .itemName(item.getItemName())
          .discountPrice(discountPrice)
          .build());
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }
  }
}
