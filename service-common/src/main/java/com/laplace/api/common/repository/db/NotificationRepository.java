package com.laplace.api.common.repository.db;

import com.laplace.api.common.constants.enums.NotificationType;
import com.laplace.api.common.model.db.Notification;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

  boolean existsByFromUserIdAndItemIdAndType(Integer fromUserId, String itemId,
      NotificationType type);

  Optional<Notification> findByIdAndUserId(String notificationId, Integer userId);

  Page<Notification> findByUserId(Integer userId, Pageable page);

  List<Notification> findByTypeAndItemIdIn(NotificationType Type, Set<String> ids);
}
