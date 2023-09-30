package com.laplace.api.common.dto.response;

import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.model.db.Item;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPackageResponseDTO {

  private String requestId;
  private List<ItemMinimumResponseDTO> items;

  public static ItemPackageResponseDTO from(ItemPackageRequestDTO itemPackageRequestDTO,
      List<Item> itemList) {
    return ItemPackageResponseDTO.builder()
        .requestId(itemPackageRequestDTO.getPackageId())
        .items(itemList.stream()
            .map(item -> ItemMinimumResponseDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .build())
            .collect(Collectors.toList()))
        .build();

  }
}