package com.laplace.api.common.service.impl;

import static com.laplace.api.common.constants.ApplicationConstants.JUDGEMENT_STATUS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.StatusConstants;
import com.laplace.api.common.constants.enums.CumulativeItemStatus;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.MyClosetResponseDTO;
import com.laplace.api.common.dto.response.SellClosetResponseDTO;
import com.laplace.api.common.model.db.Favorite;
import com.laplace.api.common.model.db.InitialSettings;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.repository.db.FavoriteRepository;
import com.laplace.api.common.repository.db.ItemRepository;
import com.laplace.api.common.service.ClosetService;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.common.service.NotificationService;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class ClosetServiceImpl implements ClosetService {

  private final ItemRepository itemsRepository;
  private final FavoriteRepository favoriteRepository;
  private final InitialSettingsService initialSettingsService;
  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  @Autowired
  private ClosetServiceImpl(ItemRepository itemsRepository,
      FavoriteRepository favoriteRepository,
      InitialSettingsService initialSettingsService,
      NotificationService notificationService,
      ObjectMapper objectMapper) {
    this.itemsRepository = itemsRepository;
    this.favoriteRepository = favoriteRepository;
    this.initialSettingsService = initialSettingsService;
    this.notificationService = notificationService;
    this.objectMapper = objectMapper;
  }

  @Override
  public PageableResponseDTO<MyClosetResponseDTO> getMyCloset(Pageable pageable, Integer userId,
      Set<ItemStatus> itemStatuses) {
    Set<ItemStatus> MY_CLOSET_STATUSES = addMyClosetStatues(itemStatuses);
    Page<Item> itemPage = itemsRepository
        .findAllByStatusInAndCreatedByAndHiddenIsFalseAndBlacklistedIsFalseOrderByCreatedOnDesc(
            MY_CLOSET_STATUSES, userId, pageable);

    return PageableResponseDTO
        .create(itemPage.getTotalElements(), itemPage.getTotalPages(), itemPage.stream()
            .map(item -> MyClosetResponseDTO.from(item, StringUtils.EMPTY_STRING))
            .collect(Collectors.toList()));
  }

  @Override
  public PageableResponseDTO<SellClosetResponseDTO> getSellCloset(Pageable pageable, Integer userId,
      Set<CumulativeItemStatus> cumulativeItemStatuses) {
    Set<ItemStatus> SELL_CLOSET_STATUSES = addSellClosetStatues(cumulativeItemStatuses);
    Page<Item> itemPage;
    if (ObjectUtils.isEmpty(cumulativeItemStatuses)) {
      pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
          Sort.by(Direction.DESC, JUDGEMENT_STATUS));
      itemPage = itemsRepository
          .findAllByCreatedByAndStatusInAndHiddenIsFalseAndBlacklistedIsFalseOrCreatedByAndJudgementStatusAndHiddenIsFalseAndBlacklistedIsFalseOrderByDisplayRequestDateDesc(
              userId, SELL_CLOSET_STATUSES, userId, JudgementStatus.FAKE, pageable);
    } else {
      itemPage = itemsRepository
          .findAllByStatusInAndCreatedByAndHiddenIsFalseAndBlacklistedIsFalseOrderByDisplayRequestDateDesc(
              SELL_CLOSET_STATUSES, userId, pageable);
    }
    Integer displayPeriod = initialSettingsService.getInitialSettings().
        map(InitialSettings::getDisplayPeriod).orElse(ApplicationConstants.OVER_SALE_MONTH);

    Set<String> itemIds = itemPage.stream().map(Item::getItemId).collect(Collectors.toSet());

    Map<String, List<Notification>> notificationMap = notificationService
        .getNegotiationNotificationByIds(itemIds)
        .stream().collect(Collectors.groupingBy(Notification::getItemId));

    return PageableResponseDTO
        .create(itemPage.getTotalElements(), itemPage.getTotalPages(), itemPage.stream()
            .map(item -> SellClosetResponseDTO.from(item, displayPeriod,
                notificationMap.get(item.getItemId()), objectMapper))
            .sorted(Comparator.comparing(SellClosetResponseDTO::getJudgementStatus))
            .collect(Collectors.toList()));
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> getFavoriteCloset(Integer userId,
      Pageable pageable) {
    List<String> favoriteItemIds = favoriteRepository.findByUserIdOrderByCreatedOnDesc(userId)
        .stream()
        .map(Favorite::getItemId)
        .collect(Collectors.toList());
    Page<Item> itemPage = itemsRepository
        .findAllByItemIdInAndHiddenIsFalseAndBlacklistedIsFalse(favoriteItemIds, pageable);
    return PageableResponseDTO
        .create(itemPage.getTotalElements(), itemPage.getTotalPages(), itemPage.stream()
            .map(item -> ItemBasicResponseDto.from(item, true))
            .sorted(Comparator.comparing(item -> favoriteItemIds.indexOf(item.getItemId())))
            .collect(Collectors.toList()));
  }

  private Set<ItemStatus> addMyClosetStatues(Set<ItemStatus> itemStatuses) {
    if (ObjectUtils.isEmpty(itemStatuses)) {
      return StatusConstants.MY_CLOSET_ITEMS_STATUSES;
    } else {
      return itemStatuses;
    }
  }

  private Set<ItemStatus> addSellClosetStatues(Set<CumulativeItemStatus> cumulativeItemStatuses) {
    if (ObjectUtils.isEmpty(cumulativeItemStatuses)) {
      return StatusConstants.SELL_CLOSET_ITEMS_STATUSES;
    } else {
      return convertCumulativeToItemStatus(cumulativeItemStatuses);
    }
  }

  private Set<ItemStatus> convertCumulativeToItemStatus(
      Set<CumulativeItemStatus> cumulativeItemStatuses) {
    Set<ItemStatus> SELL_CLOSET_STATUSES = new HashSet<>();

    cumulativeItemStatuses.forEach(status -> {
      switch (status) {
        case SOLD_OUT: {
          SELL_CLOSET_STATUSES.addAll(StatusConstants.SOLD_OUT_STATUSES);
          break;
        }
        case CURRENTLY_SELLING: {
          SELL_CLOSET_STATUSES.addAll(StatusConstants.DISPLAYABLE_STATUSES);
          break;
        }
      }
    });
    return SELL_CLOSET_STATUSES;
  }
}
