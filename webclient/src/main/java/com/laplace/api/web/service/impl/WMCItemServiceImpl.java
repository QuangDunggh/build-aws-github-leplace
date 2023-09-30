package com.laplace.api.web.service.impl;

import static com.laplace.api.common.constants.StatusConstants.DISPLAYABLE_STATUSES;
import static com.laplace.api.common.constants.StatusConstants.PRE_ON_SALE_STATUSES;
import static com.laplace.api.common.constants.StatusConstants.SELLING_STATUSES_STRING;
import static com.laplace.api.common.constants.StatusConstants.SOLD_OUT_STATUSES;
import static com.laplace.api.common.constants.StatusConstants.SOLD_OUT_STATUSES_STRING;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.BLACKLISTED;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.USER_NOT_EXISTS;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.StatusConstants;
import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.FilteringCriteria;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.NotificationType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.SearchSortType;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.converter.NotificationConverter;
import com.laplace.api.common.converter.response.SellConditionConverter;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.dto.notification.DiscountNotificationDTO;
import com.laplace.api.common.dto.notification.ItemSoldNotificationDTO;
import com.laplace.api.common.dto.request.SellConditionRequestDto;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.ItemDetailsResponse;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.Brand;
import com.laplace.api.common.model.db.InitialSettings;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.BrandsService;
import com.laplace.api.common.service.FavoriteService;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.service.LaplaceLambdaService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.core.dto.SellerItemStatus;
import com.laplace.api.web.service.WMCItemService;
import com.laplace.api.web.service.facade.ItemFacade;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.elasticsearch.common.util.set.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WMCItemServiceImpl implements WMCItemService {

  private final AuthenticationFacade authenticationFacade;
  private final ItemService itemService;
  private final AppUserService appUserService;
  private final SellConditionConverter sellConditionConverter;
  private final ItemFacade itemFacade;
  private final BrandsService brandsService;
  private final FavoriteService favoriteService;
  private final InitialSettingsService initialSettingsService;
  private final LaplaceLambdaService laplaceLambdaService;
  private final NotificationConverter notificationConverter;
  private final MailConfiguration mailConfiguration;
  private final ObjectMapper objectMapper;

  @Autowired
  public WMCItemServiceImpl(AuthenticationFacade authenticationFacade,
      ItemService itemService, AppUserService appUserService,
      SellConditionConverter sellConditionConverter,
      ItemFacade itemFacade, BrandsService brandsService,
      FavoriteService favoriteService,
      InitialSettingsService initialSettingsService,
      LaplaceLambdaService laplaceLambdaService,
      NotificationConverter notificationConverter,
      MailConfiguration mailConfiguration, ObjectMapper objectMapper) {
    this.authenticationFacade = authenticationFacade;
    this.itemService = itemService;
    this.appUserService = appUserService;
    this.sellConditionConverter = sellConditionConverter;
    this.itemFacade = itemFacade;
    this.brandsService = brandsService;
    this.favoriteService = favoriteService;
    this.initialSettingsService = initialSettingsService;
    this.laplaceLambdaService = laplaceLambdaService;
    this.notificationConverter = notificationConverter;
    this.mailConfiguration = mailConfiguration;
    this.objectMapper = objectMapper;
  }

  @Override
  public ItemDetailsResponse getItemDetails(Integer userId, String id) {
    Item item = itemService.wmcFindById(id)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));
    if ((userId.equals(ApplicationConstants.ANONYMOUS_USER) || !item.getSellerId().equals(userId))
        && !DISPLAYABLE_STATUSES.contains(item.getStatus()) && !SOLD_OUT_STATUSES
        .contains(item.getStatus())) {
      throw throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND);
    }

    String email = appUserService.findEmailById(item.getSellerId())
        .orElse(StringUtils.EMPTY_STRING);
    InitialSettings initialSettings = initialSettingsService.getInitialSettings().orElse(null);

    return ItemDetailsResponse.from(item, email, InitialSettingsDTO.from(initialSettings),
        favoriteService.isFavoriteByUserIdAndItemId(userId, item.getItemId()));
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> findItemsByCriteria(
      FilteringCriteria filteringCriteria, Integer userId, Pageable page) {

    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.PUBLISHED_AT)));
    Page<Item> itemsPage;
    switch (filteringCriteria) {
      case MEN: {
        itemsPage = itemService.findItemsByAudience(TargetAudience.MEN, request);
        break;
      }

      case WOMEN: {
        itemsPage = itemService.findItemsByAudience(TargetAudience.WOMEN, request);
        break;
      }

      case PICK_UP: {
        request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
            page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.PICKUP_AT)));
        itemsPage = itemService.findItemsByPickUp(request);
        break;
      }
      default: {
        throw throwApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
      }
    }

    Map<String, Boolean> favoritesByUserId = favoriteService.findFavoritesByUserId(userId,
        itemsPage.get().map(Item::getItemId).collect(Collectors.toSet()));
    return PageableResponseDTO
        .create(itemsPage.getTotalElements(), itemsPage.getTotalPages(), itemsPage.stream()
            .map(item -> ItemBasicResponseDto
                .from(item, favoritesByUserId.get(item.getItemId())))
            .collect(Collectors.toList()));
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> findNewArrivalItems(
      Integer userId, Pageable page) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        Sort.by(Sort.Direction.DESC, ApplicationConstants.PUBLISHED_AT));
    return findItemWithSorting(userId, request);
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> itemSearch(Integer userId,
      ItemSearchRequest itemSearchRequest) {
    itemSearchRequest.setHidden(false);
    itemSearchRequest.setIsNotBlacklisted(true);
    if (ObjectUtils.isEmpty(itemSearchRequest.getJudgementStatus())) {
      itemSearchRequest.setJudgementStatus(JudgementStatus.GENUINE);
    }

    if (BooleanUtils.isTrue(itemSearchRequest.getStock())) {
      if (ObjectUtils.isEmpty(itemSearchRequest.getStatus())) {
        itemSearchRequest.setStatus(StatusConstants.TOP_PAGE_DISPLAYABLE_STATUSES_STRING);
      } else {
        itemSearchRequest.getStatus().addAll(StatusConstants.TOP_PAGE_DISPLAYABLE_STATUSES_STRING);
      }
    }

    if (BooleanUtils.isTrue(itemSearchRequest.getSoldOut())) {
      if (ObjectUtils.isEmpty(itemSearchRequest.getStatus())) {
        itemSearchRequest.setStatus(SOLD_OUT_STATUSES_STRING);
      } else {
        itemSearchRequest.getStatus().addAll(StatusConstants.SOLD_OUT_STATUSES_STRING);
      }
    }

    if(ObjectUtils.isEmpty(itemSearchRequest.getStatus())) {
      itemSearchRequest.setStatus(StatusConstants.TOP_PAGE_DISPLAYABLE_STATUSES_STRING);
    }

    if (BooleanUtils.isTrue(itemSearchRequest.getPopularItems())) {
      Set<Integer> popularBrandIds = brandsService.findAllPopularBrands()
          .stream().map(Brand::getBrandId).collect(Collectors.toSet());
      if (ObjectUtils.isEmpty(itemSearchRequest.getBrands())) {
        itemSearchRequest.setBrands(popularBrandIds);
      } else {
        Set<Integer> selectedBrands = Sets
            .intersection(popularBrandIds, itemSearchRequest.getBrands());
        itemSearchRequest.setBrands(selectedBrands);
      }
    }

    Page<ItemDocument> itemsPage = itemService.search(itemSearchRequest);
    Map<String, Boolean> favoritesByUserId = favoriteService.findFavoritesByUserId(userId,
        itemsPage.get().map(ItemDocument::getId).collect(Collectors.toSet()));

    return PageableResponseDTO
        .create(itemsPage.getTotalElements(), itemsPage.getTotalPages(), itemsPage.stream()
            .map(item -> ItemBasicResponseDto
                .from(item, favoritesByUserId.get(item.getId())))
            .collect(Collectors.toList()));
  }

  @Override
  public void updateItem(String id, SellConditionRequestDto dto) {
    Item item = itemFacade.findItemAndValidateSeller(id, authenticationFacade.getUserId());

    if (!PRE_ON_SALE_STATUSES.contains(item.getStatus())) {
      throw throwApplicationException(ResultCodeConstants.NOT_PERMITTED_STATUS);
    }
    item.setLastUpdatedBy(authenticationFacade.getUserId());
    item.setLastUpdatedOn(DateUtil.timeNow());
    if (DISPLAYABLE_STATUSES.contains(item.getStatus())
        && item.getDisplayPrice() > dto.getSellingPrice()) {
      laplaceLambdaService.itemDiscountNotifyHandler(
          notificationConverter.convert(buildDiscountNotificationDto(item, dto.getSellingPrice())));
    }
    itemService.save(sellConditionConverter.makeItem(dto, item));
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> popularDesignerItems(Integer userId,
      Integer page, Integer size,
      Integer sort) {
    Set<Integer> popularBrandIds = brandsService.findAllPopularBrands()
        .stream().map(Brand::getBrandId).collect(Collectors.toSet());
    if (popularBrandIds.isEmpty()) {
      return new PageableResponseDTO<>();
    } else {
      ItemSearchRequest searchRequest = ItemSearchRequest.builder()
          .brands(popularBrandIds)
          .page(page)
          .size(size)
          .sort(SearchSortType.forValue(sort))
          .build();
      return itemSearch(userId, searchRequest);
    }
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> findUserChoiceItems(Integer userId,
      Pageable page) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.FAVORITE_COUNT)));
    return findItemWithSorting(userId, request);
  }

  @Override
  public Optional<Item> findItem(String itemId) {
    return itemService.wmcFindById(itemId);
  }

  @Override
  public void updateFavCount(String id, Item item, boolean isIncrement) {
    itemService.updateFavCount(id, item, isIncrement);
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> getRelatedItems(Integer userId, String itemId,
      Integer brandId,
      Category category, Pageable pageable) {
    ItemSearchRequest itemSearchRequest = ItemSearchRequest.builder()
        .notInItemId(itemId)
        .status(StatusConstants.SELLING_STATUSES_STRING)
        .category(category)
        .brands(Collections.singleton(brandId))
        .sort(SearchSortType.DISPLAY_DATE_DESC)
        .page(pageable.getPageNumber())
        .size(pageable.getPageSize())
        .build();
    return itemSearch(userId, itemSearchRequest);
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> getSellerAllItems(Integer id, Integer userId,
      Set<SellerItemStatus> sellerItemStatus, Pageable pageable) {
    AppUser appUser = appUserService.findById(id)
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    if (appUser.getUserStatus().equals(UserStatus.BLACK_LISTED)) {
      throw throwApplicationException(BLACKLISTED);
    }
    ItemSearchRequest itemSearchRequest = ItemSearchRequest.builder()
        .sellerId(id)
        .status(convertSellerStatus(sellerItemStatus))
        .sort(SearchSortType.DISPLAY_DATE_DESC)
        .page(pageable.getPageNumber())
        .size(pageable.getPageSize())
        .build();
    return itemSearch(userId, itemSearchRequest);
  }

  private PageableResponseDTO<ItemBasicResponseDto> findItemWithSorting(Integer userId,
      Pageable page) {
    Page<Item> itemPage = itemService.findDisplayableItems(page);
    Map<String, Boolean> favoritesByUserId = favoriteService.findFavoritesByUserId(userId,
        itemPage.get().map(Item::getItemId).collect(Collectors.toSet()));
    return PageableResponseDTO
        .create(itemPage.getTotalElements(), itemPage.getTotalPages(), itemPage.stream()
            .map(item -> ItemBasicResponseDto
                .from(item, favoritesByUserId.get(item.getItemId())))
            .collect(Collectors.toList()));
  }

  private Set<String> convertSellerStatus(Set<SellerItemStatus> sellerItemStatus) {
    Set<String> SELLER_ITEMS_STATUSES = new HashSet<String>();

    if (ObjectUtils.isEmpty(sellerItemStatus)) {
      SELLER_ITEMS_STATUSES.addAll(StatusConstants.SOLD_OUT_STATUSES_STRING);
      SELLER_ITEMS_STATUSES.addAll(StatusConstants.SELLING_STATUSES_STRING);
      return SELLER_ITEMS_STATUSES;
    }
    sellerItemStatus.forEach(status -> {
      switch (status) {
        case SOLD_OUT: {
          SELLER_ITEMS_STATUSES.addAll(SOLD_OUT_STATUSES_STRING);
          break;
        }
        case SELLING: {
          SELLER_ITEMS_STATUSES.addAll(SELLING_STATUSES_STRING);
          break;
        }
      }
    });
    return SELLER_ITEMS_STATUSES;
  }

  private NotificationDto buildDiscountNotificationDto(Item item, Integer discountPrice) {
    return NotificationDto.builder()
        .fromUserId(item.getSellerId())
        .itemId(item.getItemId())
        .dataOfMessage(
            DiscountNotificationDTO.makeJson(objectMapper, item, discountPrice,
                mailConfiguration.getWmcItemUrl() + item.getItemId()))
        .type(NotificationType.DISCOUNT_NOTIFY)
        .build();
  }
}