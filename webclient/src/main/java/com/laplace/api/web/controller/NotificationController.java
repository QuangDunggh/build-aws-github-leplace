package com.laplace.api.web.controller;

import com.laplace.api.common.dto.request.RemindingExpireDateRequestDTO;
import com.laplace.api.common.service.NotificationService;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
@RequestMapping(APIEndPoints.NOTIFICATION)
public class NotificationController {

  private final NotificationService notificationService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public NotificationController(
      NotificationService notificationService,
      AuthenticationFacade authenticationFacade) {
    this.notificationService = notificationService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse getNotificationDetails(@PathVariable("id") String notificationId) {
    return BaseResponse.create(notificationService
        .getNotificationDetails(notificationId, authenticationFacade.getUserId()));
  }

  @PatchMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse readNotification(@PathVariable("id") String notificationId) {
    return BaseResponse.create(notificationService.readNotification(notificationId, authenticationFacade.getUserId()));
  }

  @PutMapping(APIEndPoints.REMINDING_EXPIRED_DATE + APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse updateRemindingExpireDateNotification(
      @PathVariable("id") String notificationId,
      @RequestBody @Valid RemindingExpireDateRequestDTO remindingExpireDateRequestDTO) {
    notificationService.updateRemindingExpireDateNotification(notificationId,
        remindingExpireDateRequestDTO, authenticationFacade.getUserId());
    return BaseResponse.create();
  }

  @GetMapping
  public BaseResponse getNotifications(Pageable page) {
    return BaseResponse
        .create(notificationService.findNotifications(page, authenticationFacade.getUserId()));
  }
}
