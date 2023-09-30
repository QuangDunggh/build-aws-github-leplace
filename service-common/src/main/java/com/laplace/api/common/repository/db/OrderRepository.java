package com.laplace.api.common.repository.db;

import com.laplace.api.common.constants.enums.DeliveryType;
import com.laplace.api.common.constants.enums.OrderStatus;
import com.laplace.api.common.model.db.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, String> {

  Page<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(ZonedDateTime startTime, ZonedDateTime endTime,
      Pageable pageRequest);

  Page<Order> findByUserId(Integer userId, Pageable pageable);

  Page<Order> findByUserIdAndConfirmedTrue(Integer userId, Pageable pageable);

  Optional<Order> findByOrderIdAndUserId(String orderId, Integer userId);

  Optional<Order> findByOrderIdAndUserIdAndConfirmedTrue(String orderId, Integer userId);

  Optional<Order> findByOrderIdAndUserIdAndStatusAndConfirmedTrue(String orderId, Integer userId,
      OrderStatus orderStatus);

  Optional<Order> findByOrderIdAndSellerId(String orderId, Integer sellerId);

  Page<Order> findByUserIdAndDeliveryTypeInAndStatus(Integer userId,
      Set<DeliveryType> deliveryTypes, OrderStatus orderStatus, Pageable request);

  Page<Order> findByCreatedAtBetweenAndSellerIdOrderByCreatedAtDesc(ZonedDateTime firstDayOfMonth,
      ZonedDateTime lastDayOfMonth, Integer userId, Pageable pageable);

  Page<Order> findByCreatedAtBetweenAndSellerId(ZonedDateTime firstDayOfMonth,
      ZonedDateTime lastDayOfMonth, Integer userId, Pageable pageable);

  Page<Order> findByCreatedAtBetweenAndUserId(ZonedDateTime firstDayOfMonth,
      ZonedDateTime lastDayOfMonth, Integer userId, Pageable pageable);

  Page<Order> findByCreatedAtBetweenAndSellerIdAndStatusIn(ZonedDateTime firstDayOfMonth,
      ZonedDateTime lastDayOfMonth, Integer userId, Set<OrderStatus> statuses, Pageable pageable);

  Set<Order> findAllByOrderIdIn(Set<String> itemIds);

  Optional<Order> findByItemIdAndStatusIn(String itemId, Set<OrderStatus> orderStatuses);

  Optional<Order> findByItemIdAndSellerIdAndConfirmedFalse(String itemId, Integer sellerId);

  boolean existsByUserIdAndDeliveryTypeIn(Integer userId, Set<DeliveryType> deliveryTypes);

  Optional<Order> findByItemIdAndUserIdAndStatusAndConfirmedFalse(String itemId, Integer userId, OrderStatus orderStatus);

  @Modifying
  @Transactional
  @Query(value = "update orders_laplace set delivery_status=:deliveryType where item_id=:itemId", nativeQuery = true)
  void updateDeliveryType(@Param("itemId") String itemId, @Param("deliveryType") String deliveryType);
}
