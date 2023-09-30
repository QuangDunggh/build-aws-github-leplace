package com.laplace.api.cms.service.pkg;

import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.TypeOfUser;
import com.laplace.api.common.dto.response.OrderResponseDetailsDto;
import com.laplace.api.common.dto.response.OrderResponseDto;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.util.PageableResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface OrderService {

  PageableResponseDTO<OrderResponseDto> getAllOrders(Integer month, Integer year, Integer userId,
      TypeOfUser typeOfUser, Pageable pageable);

  OrderResponseDetailsDto getOrderDetailsById(String orderId);

  Optional<Order> getOrderByItemId(String itemId);

  Map<String, Item> findItemByIds(Set<String> orderIds);

  void updateDeliveryType(String itemId, ItemStatus status);
}
