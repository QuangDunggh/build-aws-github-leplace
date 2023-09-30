package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.NotificationType;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.dto.request.RemindingExpireDateRequestDTO;
import com.laplace.api.common.dto.response.NotificationBasicResponseDto;
import com.laplace.api.common.dto.response.NotificationDetailsResponseDto;
import com.laplace.api.common.dto.response.projection.UnreadCountProjection;
import com.laplace.api.common.model.db.Notification;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

  void saveNotification(NotificationDto notificationDto);

  boolean existsByFromUserIdAndItemIdAndType(Integer fromUserId, String itemId,
      NotificationType type);

  UnreadCountProjection readNotification(String notificationId, Integer userId);

  PageableResponseDTO<NotificationBasicResponseDto> findNotifications(Pageable page,
      Integer userId);

  NotificationDetailsResponseDto getNotificationDetails(String notificationId, Integer userId);

  void updateRemindingExpireDateNotification(String notificationId,
      RemindingExpireDateRequestDTO request, Integer userId);

  List<Notification> getNegotiationNotificationByIds(Set<String> ids);
}
