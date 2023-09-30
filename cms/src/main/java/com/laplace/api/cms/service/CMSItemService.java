package com.laplace.api.cms.service;

import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.dto.ItemImageDTO;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.ItemPickUpAndHiddenRequestDto;
import com.laplace.api.common.dto.request.ItemRequestDto;
import com.laplace.api.common.dto.request.ItemStatusChangeRequestDto;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.ItemDetailsResponse;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

public interface CMSItemService {

  void updateItem(String id, ItemRequestDto itemRequestDto);

  void uploadItemImages(String id, Set<ItemImageDTO> imageDTO);

  ItemDetailsResponse getItemDetails(String id);

  PageableResponseDTO<ItemBasicResponseDto> getItemList(String keyword,
      List<ItemStatus> statuses,
      Pageable pageable);

  ItemDetailsResponse validateAndUpdateStatus(ItemStatusChangeRequestDto requestDto);

  ItemDetailsResponse updatePickUpAndHiddenStatus(ItemPickUpAndHiddenRequestDto requestDto);

  @Async("taskExecutor")
  void syncItems();

  PageableResponseDTO<ItemBasicResponseDto> searchItems(ItemSearchRequest itemSearchRequest);
}
