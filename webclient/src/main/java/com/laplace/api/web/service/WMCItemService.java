package com.laplace.api.web.service;

import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.FilteringCriteria;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.SellConditionRequestDto;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.ItemDetailsResponse;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.web.core.dto.SellerItemStatus;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface WMCItemService {

  ItemDetailsResponse getItemDetails(Integer userId, String id);

  PageableResponseDTO<ItemBasicResponseDto> findItemsByCriteria(FilteringCriteria filteringCriteria,
      Integer userId, Pageable pageable);

  PageableResponseDTO<ItemBasicResponseDto> findNewArrivalItems(Integer userId,
      Pageable page);

  PageableResponseDTO<ItemBasicResponseDto> itemSearch(Integer userId,
      ItemSearchRequest itemSearchRequest);

  void updateItem(String id, SellConditionRequestDto sellConditionRequestDto);

  PageableResponseDTO<ItemBasicResponseDto> popularDesignerItems(Integer userId,
      Integer page, Integer size, Integer sort);

  PageableResponseDTO<ItemBasicResponseDto> findUserChoiceItems(Integer userId,
      Pageable page);

  Optional<Item> findItem(String itemId);

  void updateFavCount(String id, Item item, boolean isIncrement);

  PageableResponseDTO<ItemBasicResponseDto> getRelatedItems(Integer userId, String itemId,
      Integer brandId, Category category, Pageable pageable);

  PageableResponseDTO<ItemBasicResponseDto> getSellerAllItems(Integer id, Integer userId,
      Set<SellerItemStatus> sellerItemStatus, Pageable pageable);
}
