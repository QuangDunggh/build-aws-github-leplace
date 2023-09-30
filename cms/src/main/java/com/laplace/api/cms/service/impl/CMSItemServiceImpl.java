package com.laplace.api.cms.service.impl;

import com.laplace.api.cms.service.CMSEmailService;
import com.laplace.api.cms.service.CMSItemService;
import com.laplace.api.cms.service.pkg.OrderService;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.StatusConstants;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.converter.response.ItemImageConverter;
import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.dto.ItemImageDTO;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.ItemPickUpAndHiddenRequestDto;
import com.laplace.api.common.dto.request.ItemRequestDto;
import com.laplace.api.common.dto.request.ItemStatusChangeRequestDto;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.ItemDetailsResponse;
import com.laplace.api.common.model.db.*;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import com.laplace.api.common.repository.db.OrderRepository;
import com.laplace.api.common.service.AddressService;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.laplace.api.common.constants.StatusConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Slf4j
@Service
public class CMSItemServiceImpl implements CMSItemService {

  private final ItemService itemService;
  private final AuthenticationFacade authenticationFacade;
  private final AppUserService appUserService;
  private final CMSEmailService cmsEmailService;
  private final ItemImageConverter itemImageConverter;
  private final OrderService orderService;
  private final InitialSettingsService initialSettingsService;
  private final AddressService addressService;
  private final OrderRepository orderRepository;

  @Autowired
  public CMSItemServiceImpl(ItemService itemService,
      AuthenticationFacade authenticationFacade,
      AppUserService appUserService, CMSEmailService cmsEmailService,
      ItemImageConverter itemImageConverter,
      OrderService orderService,
      InitialSettingsService initialSettingsService,
      AddressService addressService,
      OrderRepository orderRepository) {
    this.itemService = itemService;
    this.authenticationFacade = authenticationFacade;
    this.appUserService = appUserService;
    this.cmsEmailService = cmsEmailService;
    this.itemImageConverter = itemImageConverter;
    this.orderService = orderService;
    this.initialSettingsService = initialSettingsService;
    this.addressService = addressService;
    this.orderRepository = orderRepository;
  }

  @Override
  public void updateItem(String itemId, ItemRequestDto itemRequestDto) {
    Item item = itemService.findById(itemId).orElseThrow(
        () -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND)
    );
    if (item.getStatus().equals(ItemStatus.ITEM_INSPECTION) && !item.getImages().isEmpty()) {
      item.setStatus(ItemStatus.ON_SALE);
      appUserService.incrementItemsOnDisplayCount(item.getSellerId());
      item.setDisplayRequestDate(DateUtil.timeNow());
      AppUser appUser = appUserService.findById(item.getSellerId())
          .orElseThrow(() -> throwApplicationException(ResultCodeConstants.EMAIL_NOT_SENT));
      cmsEmailService.sendOnSaleMail(item, appUser);
    }
    itemRequestDto.setLastUpdatedBy(authenticationFacade.getUserId());
    itemService.validateAndUpdate(item, itemRequestDto);

  }

  @Override
  public void uploadItemImages(String id, Set<ItemImageDTO> imageDTO) {
    Item item = itemService.findById(id)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));
    Set<ItemImage> itemImages = imageDTO.stream()
        .map(dto -> {
          if (Boolean.TRUE.equals(dto.getIsCoverImage())) {
            item.setCoverImage(dto.getImageUrl());
          }
          return itemImageConverter.makeItemImage(dto, id);
        })
        .collect(Collectors.toSet());
    item.getImages().clear();
    item.getImages().addAll(itemImages);
    updateItem(item);
  }

  @Override
  public ItemDetailsResponse getItemDetails(String id) {
    Item item = itemService.findById(id)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));
    String email = appUserService.findEmailById(item.getSellerId())
        .orElse(StringUtils.EMPTY_STRING);
    InitialSettings initialSettings = initialSettingsService.getInitialSettings().orElse(null);
    Address address = addressService.getAddressById(item.getAddressId());
    AppUser user = appUserService.findById(item.getSellerId()).orElse(null);
    Order order = orderRepository.findByItemIdAndStatusIn(id, ORDER_CONFIRM_STATUSES).orElse(null);
    GiftWrappingDTO giftWrappingDTO = ObjectUtils.isEmpty(order) ? GiftWrappingDTO.from(null)
        : initialSettingsService.getGiftWrappingDetails(order.getGiftWrappingOptions());

    AppUserProfile userProfile =
        (!ObjectUtils.isEmpty(user)) ? user.getOrEmptyProfile() : AppUserProfile.emptyProfile();
    return ItemDetailsResponse.from(item, email, InitialSettingsDTO.from(initialSettings),
        userProfile, address, giftWrappingDTO);
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> getItemList(String keyword,
      List<ItemStatus> statuses, Pageable page) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, "displayRequestDate")));

    Page<Item> itemInfoPage = itemService.findItemByPage(keyword, statuses, request);
    List<Item> itemsList = itemInfoPage.getContent();
    Map<Integer, String> emailMap = appUserService.findEmailMapByIds(itemsList.stream()
        .map(Item::getSellerId).collect(Collectors.toSet()));
    return PageableResponseDTO.create(itemInfoPage.getTotalElements(), itemInfoPage.getTotalPages(),
        itemsList.stream().map(
                ItemBasicResponseDto::from)
            .collect(Collectors.toList()));
  }

  @Override
  public ItemDetailsResponse validateAndUpdateStatus(ItemStatusChangeRequestDto requestDto) {
    boolean judgementMailSendingNeeded = false;
    boolean statusChangeMailSendingNeeded = false;
    Item item = itemService.findById(requestDto.getItemId())
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));
    InitialSettings initialSettings = initialSettingsService.getInitialSettings().orElse(null);

    if (!ObjectUtils.isEmpty(requestDto.getStatus())) {
      statusChangeMailSendingNeeded = performActionBasedOnStatus(item, requestDto);
    }

    if (!ObjectUtils.isEmpty(requestDto.getJudgementStatus())) {
      judgementMailSendingNeeded = updateJudgementStatus(item, requestDto.getJudgementStatus());
    }

    if (!ObjectUtils.isEmpty(requestDto.getExpectedDateTime())) {
      item.setExpectedDateTime(DateUtil.fromEpochMilli(requestDto.getExpectedDateTime()));
    }

    item = updateItem(item);
    if (judgementMailSendingNeeded) {
      AppUser user = appUserService.findById(item.getSellerId()).orElseThrow(
          () -> throwApplicationException(ResultCodeConstants.EMAIL_NOT_SENT)
      );
      cmsEmailService.sendFakeJudgementMail(item, user);
    }

    if (statusChangeMailSendingNeeded &&
        item.getJudgementStatus().equals(JudgementStatus.GENUINE)) {
      try {
        AppUser user = appUserService.findById(item.getSellerId())
            .orElseThrow(() -> throwApplicationException(ResultCodeConstants.EMAIL_NOT_SENT));
        switch (item.getStatus()) {
          case ON_SALE: {
            cmsEmailService.sendOnSaleMail(item, user);
            break;
          }
          case ON_THE_WAY_TO_BUYER: {
            Order order = orderService.getOrderByItemId(item.getItemId())
                .orElseThrow(() -> throwApplicationException(ResultCodeConstants.EMAIL_NOT_SENT));
            cmsEmailService.sendReadyToDeliveredMail(order, user);
            break;
          }
          case DISPLAY_CANCELLED: {
            Order order = orderService.getOrderByItemId(item.getItemId())
                .orElseThrow(() -> throwApplicationException(ResultCodeConstants.EMAIL_NOT_SENT));
            cmsEmailService.sendItemReturnCompletedMail(order, user);
            break;
          }
        }
      } catch (Exception exception) {
        log.error("Item status change -> email sending failed: " + exception.getLocalizedMessage());
      }
    }
    return ItemDetailsResponse
        .from(item, StringUtils.EMPTY_STRING, InitialSettingsDTO.from(initialSettings));
  }

  @Override
  public ItemDetailsResponse updatePickUpAndHiddenStatus(ItemPickUpAndHiddenRequestDto requestDto) {
    InitialSettings initialSettings = initialSettingsService.getInitialSettings().orElse(null);
    Item item = itemService.findById(requestDto.getItemId())
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));

    if (!ObjectUtils.isEmpty(requestDto.getIsPicKup())) {
      if (!StatusConstants.PRE_ON_SALE_STATUSES.contains(item.getStatus())) {
        throw throwApplicationException(ResultCodeConstants.INVALID_REQUEST_STATUS);
      }
      item.setPickUp(requestDto.getIsPicKup());
      if (BooleanUtils.isTrue(requestDto.getIsPicKup())) {
        item.setPickUpAt(DateUtil.timeNow());
      }
    }

    if (!ObjectUtils.isEmpty(requestDto.getIsHidden()) &&
        DISPLAYABLE_STATUSES.contains(item.getStatus())) {
      if (!item.getHidden() && requestDto.getIsHidden()) {
        appUserService.decrementItemsOnDisplayCount(item.getSellerId());
      }
      else if (item.getHidden() && !requestDto.getIsHidden()) {
        appUserService.incrementItemsOnDisplayCount(item.getSellerId());
      }
      item.setHidden(requestDto.getIsHidden());
    }

    return ItemDetailsResponse
        .from(updateItem(item), StringUtils.EMPTY_STRING, InitialSettingsDTO.from(initialSettings));
  }

  @Override
  public void syncItems() {
    itemService.sync();
  }

  @Override
  public PageableResponseDTO<ItemBasicResponseDto> searchItems(
      ItemSearchRequest itemSearchRequest) {
    Page<ItemDocument> itemsPage = itemService.search(itemSearchRequest);
    return PageableResponseDTO
        .create(itemsPage.getTotalElements(), itemsPage.getTotalPages(), itemsPage.stream()
            .map(item -> ItemBasicResponseDto
                .from(item, null))
            .collect(Collectors.toList()));
  }

  private boolean performActionBasedOnStatus(Item item, ItemStatusChangeRequestDto requestDto) {
    if (isInvalidStatus(item, requestDto)) {
      throw throwApplicationException(ResultCodeConstants.NOT_PERMITTED_STATUS);
    }
    item.setStatus(requestDto.getStatus());

    if (DISPLAYABLE_STATUSES.contains(item.getStatus())) {
      if (isItemImagesEmpty(item)) {
        throw throwApplicationException(ResultCodeConstants.IMAGE_NOT_FOUND);
      }
      appUserService.incrementItemsOnDisplayCount(item.getSellerId());
      item.setDisplayRequestDate(DateUtil.timeNow());
    } else if (DELIVERABLE_STATES.contains(item.getStatus())) {
      orderService.updateDeliveryType(item.getItemId(), item.getStatus());

      if (ON_THE_WAY_STATES.contains(item.getStatus())) {
        item.setDeliverySlipNumber(Optional.ofNullable(requestDto.getPackageId())
            .orElseThrow(() -> throwApplicationException(ResultCodeConstants.INVALID_PACKAGE_ID)));
      }
    }
    return item.getStatus().equals(ItemStatus.ON_SALE) || item.getStatus()
        .equals(ItemStatus.ON_THE_WAY_TO_BUYER) || item.getStatus()
        .equals(ItemStatus.DISPLAY_CANCELLED);
  }

  private boolean isInvalidStatus(Item item, ItemStatusChangeRequestDto requestDto) {
    switch (requestDto.getStatus()) {
      case ITEM_INSPECTION: {
        return (item.getStatus() != ItemStatus.WAITING_FOR_ARRIVAL);
      }
      case ON_SALE:
      case PREPARE_TO_SEND_TO_SELLER: {
        return item.getStatus() != ItemStatus.ITEM_INSPECTION;
      }
      case PREPARE_TO_SEND_TO_BUYER:
      case ON_HOLD: {
        return !DISPLAYABLE_STATUSES.contains(item.getStatus());
      }
      case ON_THE_WAY_TO_BUYER: {
        return item.getStatus() != ItemStatus.PREPARE_TO_SEND_TO_BUYER;
      }
      case TRANSACTION_COMPLETE: {
        return item.getStatus() != ItemStatus.ON_THE_WAY_TO_BUYER;
      }
      case DISPLAY_CANCELLED: {
        return item.getStatus() != ItemStatus.ON_THE_WAY_TO_SELLER;
      }
      case ON_THE_WAY_TO_SELLER: {
        return !RETURN_TO_SELLER_STATES.contains(item.getStatus());
      }
      default: {
        return true;
      }
    }
  }

  private boolean isItemImagesEmpty(Item item) {
    return ObjectUtils.isEmpty(item.getCoverImage()) || ObjectUtils.isEmpty(item.getImages());
  }

  private boolean updateJudgementStatus(Item item, JudgementStatus judgementStatus) {
    if (item.getStatus() != ItemStatus.ITEM_INSPECTION) {
      throw throwApplicationException(ResultCodeConstants.NOT_PERMITTED_STATUS);
    }

    boolean mailSendingNeeded = false;

    switch (judgementStatus) {
      case NOT_VERIFIED: {
        throw throwApplicationException(ResultCodeConstants.INVALID_REQUEST_STATUS);
      }
      case GENUINE: {
        item.setPublishedAt(DateUtil.timeNow());
        break;
      }
      case FAKE: {
        if (item.getJudgementStatus().equals(JudgementStatus.FAKE)) {
          throw throwApplicationException(ResultCodeConstants.ALREADY_FAKE_DETECTED);
        }
        item.setStatus(ItemStatus.PREPARE_TO_SEND_TO_SELLER);
        mailSendingNeeded = true;
      }
    }
    item.setJudgementStatus(judgementStatus);
    return mailSendingNeeded;
  }

  private Item updateItem(Item item) {
    item.setLastUpdatedOn(DateUtil.timeNow());
    item.setLastUpdatedBy(authenticationFacade.getUserId());
    return itemService.save(item);
  }
}
