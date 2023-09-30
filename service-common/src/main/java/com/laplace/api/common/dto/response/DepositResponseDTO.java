package com.laplace.api.common.dto.response;

import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DepositResponseDTO {

  private Integer totalAmount;
  private List<DepositsDto> data;

  public static DepositResponseDTO from(List<Order> orders, Map<String, Item> itemMapById) {
    return DepositResponseDTO.builder()
        .data(orders.stream()
            .map(order -> DepositsDto.from(order, itemMapById.get(order.getItemId()))).collect(
                Collectors.toList()))
        .totalAmount(orders.stream()
            .map(order -> order.getPayments()
                .stream().map(Payment::getSellerAmount).mapToInt(Integer::intValue).sum())
            .mapToInt(Integer::intValue).sum())
        .build();
  }
}
