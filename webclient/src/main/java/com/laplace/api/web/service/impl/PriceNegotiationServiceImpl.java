package com.laplace.api.web.service.impl;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.USER_NOT_EXISTS;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.constants.ApplicationConstants.MyPage;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.StatusConstants;
import com.laplace.api.common.constants.enums.NotificationType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.dto.notification.NegotiationNotificationDTO;
import com.laplace.api.common.dto.request.PriceNegotiationRequestDto;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.service.NotificationService;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.service.PriceNegotiationService;
import com.laplace.api.web.service.WMCEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PriceNegotiationServiceImpl implements PriceNegotiationService {

  private final ItemService itemService;
  private final AuthenticationFacade authenticationFacade;
  private final NotificationService notificationService;
  private final MailConfiguration mailConfiguration;
  private final ObjectMapper objectMapper;
  private final AppUserService appUserService;
  private final WMCEmailService wmcEmailService;

  @Autowired
  public PriceNegotiationServiceImpl(ItemService itemService,
      AuthenticationFacade authenticationFacade,
      NotificationService notificationService,
      MailConfiguration mailConfiguration,
      ObjectMapper objectMapper,
      AppUserService appUserService,
      WMCEmailService wmcEmailService) {
    this.itemService = itemService;
    this.authenticationFacade = authenticationFacade;
    this.notificationService = notificationService;
    this.mailConfiguration = mailConfiguration;
    this.objectMapper = objectMapper;
    this.appUserService = appUserService;
    this.wmcEmailService = wmcEmailService;
  }

  @Override
  public void submitNegotiationPrice(String itemId,
      PriceNegotiationRequestDto priceNegotiationRequestDto) {
    Item item = itemService.findById(itemId).orElseThrow(() ->
        throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND));
    if (!StatusConstants.DISPLAYABLE_STATUSES.contains(item.getStatus())) {
      throw throwApplicationException(ResultCodeConstants.ITEM_NOT_FOUND);
    }

    AppUser appUser = appUserService.findById(authenticationFacade.getUserId())
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    if (notificationService
        .existsByFromUserIdAndItemIdAndType(appUser.getUserId(), itemId,
            NotificationType.NEW_NEGOTIATION)) {
      throw throwApplicationException(ResultCodeConstants.ALREADY_REQUESTED);
    }

    notificationService.saveNotification(buildNotificationDto(item, appUser,
        priceNegotiationRequestDto));
    try {
      AppUser seller = appUserService.findById(item.getSellerId())
          .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
      wmcEmailService.sendItemNegotiationMail(seller.getEmail(), item, seller.getFullName(),
          appUser.getOrEmptyProfile().getUserName(),
          priceNegotiationRequestDto.getNegotiationPrice(), Languages.JAPANESE);
    } catch (Exception ex) {
      log.error("Email sending Error: " + ex.getLocalizedMessage());
    }
  }

  private NotificationDto buildNotificationDto(Item item, AppUser appUser,
      PriceNegotiationRequestDto priceNegotiationRequestDto) {
    return NotificationDto.builder()
        .fromUserId(appUser.getUserId())
        .userId(item.getSellerId())
        .type(NotificationType.NEW_NEGOTIATION)
        .dataOfMessage(NegotiationNotificationDTO.makeJson(objectMapper,
            mailConfiguration.getWmcMyPageUrl() + MyPage.SELL_CLOSET,
            item, appUser.getAppUserProfile().getUserName(),
            priceNegotiationRequestDto.getNegotiationPrice()))
        .itemId(item.getItemId())
        .build();
  }
}
