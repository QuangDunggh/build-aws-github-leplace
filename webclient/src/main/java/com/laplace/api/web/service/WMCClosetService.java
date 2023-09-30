package com.laplace.api.web.service;

import com.laplace.api.common.constants.enums.CumulativeItemStatus;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.MyClosetResponseDTO;
import com.laplace.api.common.dto.response.SellClosetResponseDTO;
import com.laplace.api.common.service.ClosetService;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WMCClosetService {

  private final ClosetService closetService;

  @Autowired
  public WMCClosetService(ClosetService closetService) {
    this.closetService = closetService;
  }

  public PageableResponseDTO<MyClosetResponseDTO> getMyCloset(Pageable pageable, Integer userId,
      Set<ItemStatus> itemStatuses) {
    return closetService.getMyCloset(pageable, userId, itemStatuses);
  }

  public PageableResponseDTO<SellClosetResponseDTO> getSellCloset(Pageable pageable, Integer userId,
      Set<CumulativeItemStatus> cumulativeItemStatuses) {
    return closetService.getSellCloset(pageable, userId, cumulativeItemStatuses);
  }

  public PageableResponseDTO<ItemBasicResponseDto> getFavoriteCloset(Integer userId,
      Pageable pageable) {
    return closetService.getFavoriteCloset(userId, pageable);
  }
}
