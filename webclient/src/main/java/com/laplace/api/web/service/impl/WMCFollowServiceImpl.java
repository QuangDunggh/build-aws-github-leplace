package com.laplace.api.web.service.impl;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.USER_NOT_EXISTS;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.NotificationType;
import com.laplace.api.common.converter.FollowConverter;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.dto.notification.FollowerNotificationDTO;
import com.laplace.api.common.dto.request.FollowDTO;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.Follow;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.FollowService;
import com.laplace.api.common.service.NotificationService;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.service.WMCFollowService;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WMCFollowServiceImpl implements WMCFollowService {

  private final AuthenticationFacade authenticationFacade;
  private final FollowService followService;
  private final FollowConverter followConverter;
  private final NotificationService notificationService;
  private final AppUserService appUserService;
  private final ObjectMapper objectMapper;

  @Autowired
  WMCFollowServiceImpl(
      AuthenticationFacade authenticationFacade,
      FollowService followService,
      FollowConverter followConverter,
      NotificationService notificationService,
      AppUserService appUserService, ObjectMapper objectMapper) {
    this.authenticationFacade = authenticationFacade;
    this.followService = followService;
    this.followConverter = followConverter;
    this.notificationService = notificationService;
    this.appUserService = appUserService;
    this.objectMapper = objectMapper;
  }


  @Override
  public void followUser(Integer followId, FollowDTO followDTO) {
    AppUser appUser = appUserService.findById(authenticationFacade.getUserId())
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    Optional<Follow> following = followService
        .findByUserIdAndFollowedSellerId(appUser.getUserId(), followId);

    if (BooleanUtils.isFalse(followDTO.getFollow())) {
      following.ifPresent(followService::delete);
    } else if (following.isEmpty()) {
      followService.save(followConverter.make(appUser.getUserId(), followId));
      notificationService.saveNotification(buildNotificationDto(appUser, followId));
    }
  }

  @Override
  public FollowDTO isUserFollowed(Integer followId, Integer userId) {
    Optional<Follow> following = userId.equals(ApplicationConstants.ANONYMOUS_USER) ?
        Optional.empty() : followService.findByUserIdAndFollowedSellerId(userId, followId);
    FollowDTO followDTO = new FollowDTO();

    followDTO.setFollow(following.isPresent());
    return followDTO;
  }

  private NotificationDto buildNotificationDto(AppUser appUser, Integer followId) {
    return NotificationDto.builder()
        .fromUserId(appUser.getUserId())
        .userId(followId)
        .dataOfMessage(FollowerNotificationDTO.makeJson(objectMapper,
            appUser.getAppUserProfile().getUserName()))
        .type(NotificationType.NEW_FOLLOWER)
        .build();
  }
}
