package com.laplace.api.common.dto.response;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.CumulativeItemStatus;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.notification.NegotiationNotificationDTO;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.util.DateUtil;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Slf4j
@Getter
@Setter
@Builder
public class SellClosetResponseDTO {

  private String itemName;
  private String descriptions;
  private String itemId;
  private ItemStatus itemStatus;
  private String sellerEmail;
  private Long displayRequestDate;
  private Long displayEndTime;
  private Long listingRequestDate;
  private String image;
  private String brandName;
  private String size;
  private String color;
  private String dimensions;
  private Integer displayPrice;
  private Integer prevDisplayPrice;
  private Integer discountPercentage;
  private ZonedDateTime endOfPublication;
  private Long likes;
  private JudgementStatus judgementStatus;
  private CumulativeItemStatus cumulativeItemStatus;
  private Integer numberOfNegotiations;
  private Integer highestNegotiation;
  private Integer lowestNegotiation;

  public static SellClosetResponseDTO from(Item item, Integer displayPeriod) {
    return SellClosetResponseDTO.builder()
        .itemName(item.getItemName())
        .descriptions(item.getDescriptions())
        .itemId(item.getItemId())
        .image(item.getCoverImage())
        .itemStatus(item.getStatus())
        .displayPrice(item.getDisplayPrice())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .discountPercentage(item.getDiscountPercentage())
        .displayRequestDate(DateUtil.toEpochMilli(item.getDisplayRequestDate()))
        .listingRequestDate(DateUtil.toEpochMilli(item.getCreatedOn()))
        .sellerEmail(item.getSellerEmail())
        .displayEndTime(DateUtil.addMonths(item.getDisplayRequestDate(),
            (getMonth(displayPeriod) + getMonth(item.getExtendPeriod()))))
        .brandName(item.getBrandName())
        .size(item.getSize())
        .color(item.getColor())
        .dimensions(item.getDimensions())
        .endOfPublication(item.getPublishedAt())
        .likes(
            (item.getFavoriteCount() == null) ? ApplicationConstants.ZERO : item.getFavoriteCount())
        .judgementStatus(item.getJudgementStatus())
        .cumulativeItemStatus(calculateCumulativeStatus(item))
        .build();
  }

  public static SellClosetResponseDTO from(Item item, Integer displayPeriod,
      List<Notification> notifications, ObjectMapper objectMapper) {
    SellClosetResponseDTO response = from(item, displayPeriod);
    if (!ObjectUtils.isEmpty(notifications)) {
      List<Integer> negotiatedPrices = notifications.stream()
          .map(notification -> getNegotiatedPriceFromNotification(notification, objectMapper))
          .collect(Collectors.toList());

      response.setNumberOfNegotiations(notifications.size());
      response.setHighestNegotiation(Collections.max(negotiatedPrices));
      response.setLowestNegotiation(Collections.min(negotiatedPrices));
    } else {
      response.setNumberOfNegotiations(ApplicationConstants.VALUE_ZERO);
    }
    return response;
  }

  private static CumulativeItemStatus calculateCumulativeStatus(Item item) {
    if (item.getJudgementStatus().equals(JudgementStatus.FAKE)) {
      return CumulativeItemStatus.INVALID;
    }
    switch (item.getStatus()) {
      case PREPARE_TO_SEND_TO_BUYER:
      case ON_THE_WAY_TO_BUYER:
      case TRANSACTION_COMPLETE: {
        return CumulativeItemStatus.SOLD_OUT;
      }
      case ON_SALE: {
        return CumulativeItemStatus.CURRENTLY_SELLING;
      }
    }
    return null;
  }

  private static Integer getMonth(Integer month) {
    return ObjectUtils.isEmpty(month) ? 0 : month;
  }

  private static Integer getNegotiatedPriceFromNotification(Notification notification,
      ObjectMapper objectMapper) {
    try {
      NegotiationNotificationDTO dataOfMessage = objectMapper.readValue(
          notification.getDataOfMessage(), NegotiationNotificationDTO.class);
      return dataOfMessage.getDiscountRequestPrice();
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }
  }
}
