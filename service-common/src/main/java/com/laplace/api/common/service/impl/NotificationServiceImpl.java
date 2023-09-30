package com.laplace.api.common.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.NotificationType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.converter.NotificationConverter;
import com.laplace.api.common.converter.response.NotificationBasicDtoConverter;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.dto.notification.ExpireDateNotificationDTO;
import com.laplace.api.common.dto.request.RemindingExpireDateRequestDTO;
import com.laplace.api.common.dto.response.NotificationBasicResponseDto;
import com.laplace.api.common.dto.response.NotificationDetailsDtoConverter;
import com.laplace.api.common.dto.response.NotificationDetailsResponseDto;
import com.laplace.api.common.dto.response.projection.UnreadCountProjection;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.repository.db.NotificationRepository;
import com.laplace.api.common.repository.db.OrderRepository;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.NotificationService;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

  private final NotificationConverter notificationConverter;
  private final NotificationRepository notificationRepository;
  private final AppUserService appUserService;
  private final NotificationBasicDtoConverter notificationBasicDtoConverter;
  private final NotificationDetailsDtoConverter notificationDetailsDtoConverter;
  private final OrderRepository orderRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public NotificationServiceImpl(NotificationConverter notificationConverter,
      NotificationRepository notificationRepository, AppUserService appUserService,
      NotificationBasicDtoConverter notificationBasicDtoConverter,
      NotificationDetailsDtoConverter notificationDetailsDtoConverter,
      OrderRepository orderRepository, ObjectMapper objectMapper) {
    this.notificationConverter = notificationConverter;
    this.notificationRepository = notificationRepository;
    this.appUserService = appUserService;
    this.notificationBasicDtoConverter = notificationBasicDtoConverter;
    this.notificationDetailsDtoConverter = notificationDetailsDtoConverter;
    this.orderRepository = orderRepository;
    this.objectMapper = objectMapper;
  }

  @Transactional
  @Override
  public void saveNotification(NotificationDto notificationDto) {
    Notification notification = notificationConverter.convert(notificationDto);
    notificationRepository.save(Objects.requireNonNull(notification));
    appUserService.incrementUnreadCount(notification.getUserId());
  }

  @Override
  public boolean existsByFromUserIdAndItemIdAndType(Integer fromUserId, String itemId,
      NotificationType type) {
    return notificationRepository.existsByFromUserIdAndItemIdAndType(fromUserId, itemId, type);
  }

  @Transactional
  @Override
  public UnreadCountProjection readNotification(String notificationId, Integer userId) {
    Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.NOTIFICATION_NOT_FOUND));
    if (!notification.isReadStatus()) {
      notification.setReadStatus(true);
      notificationRepository.save(notification);
      appUserService.decrementUnreadCount(userId);
    }
    return appUserService.findUnreadCountById(userId);
  }

  @Override
  public PageableResponseDTO<NotificationBasicResponseDto> findNotifications(Pageable page,
      Integer userId) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.CREATED_ON)));
    Page<Notification> notifications = notificationRepository.findByUserId(userId, request);
    return PageableResponseDTO
        .create(notifications.getTotalElements(), notifications.getTotalPages(),
            notifications.stream()
                .map(notificationBasicDtoConverter::convert)
                .collect(Collectors.toList()));
  }

  @Override
  public NotificationDetailsResponseDto getNotificationDetails(String notificationId,
      Integer userId) {
    Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.NOTIFICATION_NOT_FOUND));
    return notificationDetailsDtoConverter.convert(notification);
  }

  @Override
  public void updateRemindingExpireDateNotification(String notificationId,
      RemindingExpireDateRequestDTO request, Integer userId) {
    Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.NOTIFICATION_NOT_FOUND));
    Order order = orderRepository.findByOrderIdAndUserId(request.getOrderId(), userId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.NOT_FOUND));

    try {
      ExpireDateNotificationDTO dataOfMessage = objectMapper.readValue(
          notification.getDataOfMessage(), ExpireDateNotificationDTO.class);
      dataOfMessage.setStatus(order.getStatus().toString());
      notification.setDataOfMessage(objectMapper.writeValueAsString(dataOfMessage));
    } catch (JsonProcessingException e) {
      log.error("++Error: Json creating exception: ", e);
      throw throwApplicationException(ResultCodeConstants.JSON_CREATION_ERROR);
    }

    notificationRepository.save(notification);
  }

  @Override
  public List<Notification> getNegotiationNotificationByIds(Set<String> ids) {
    return notificationRepository.findByTypeAndItemIdIn(NotificationType.NEW_NEGOTIATION, ids);
  }
}
