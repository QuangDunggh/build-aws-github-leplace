package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpendituresResponseDTO {

  private Integer totalAmount;
  private List<ExpendituresDTO> data;

  public static ExpendituresResponseDTO from(List<Order> orders, Map<String, Item> itemMapById) {
    return ExpendituresResponseDTO.builder()
        .data(orders.stream()
            .map(order -> ExpendituresDTO.from(order, itemMapById.get(order.getItemId()))).collect(
                Collectors.toList()))
        .totalAmount(orders.stream().map(Order::getOrderAmount).mapToInt(Integer::intValue).sum())
        .build();

  }
}
