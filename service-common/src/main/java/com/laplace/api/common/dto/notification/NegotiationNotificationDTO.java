package com.laplace.api.common.dto.notification;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.model.db.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class NegotiationNotificationDTO {

  private String image;
  private String sellClosetUrl;
  private String userName;
  private String itemName;
  private Integer currentPrice;
  private Integer discountRequestPrice;

  public static String makeJson(ObjectMapper objectMapper, String sellClosetUrl, Item item,
      String userName, Integer discountRequestPrice) {
    try {
      return objectMapper.writeValueAsString(NegotiationNotificationDTO.builder()
          .itemName(item.getItemName())
          .image(item.getCoverImage())
          .sellClosetUrl(sellClosetUrl)
          .userName(userName)
          .currentPrice(item.getDisplayPrice())
          .discountRequestPrice(discountRequestPrice)
          .build());
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }
  }
}
