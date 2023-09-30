package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.CumulativeItemStatus;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.MyClosetResponseDTO;
import com.laplace.api.common.dto.response.SellClosetResponseDTO;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface ClosetService {

  PageableResponseDTO<MyClosetResponseDTO> getMyCloset(Pageable pageable, Integer userId,
      Set<ItemStatus> itemStatuses);

  PageableResponseDTO<SellClosetResponseDTO> getSellCloset(Pageable pageable, Integer userId,
      Set<CumulativeItemStatus> cumulativeItemStatuses);

  PageableResponseDTO<ItemBasicResponseDto> getFavoriteCloset(Integer userId, Pageable pageable);
}
