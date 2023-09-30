package com.laplace.api.web.controller;

import com.laplace.api.common.constants.enums.CumulativeItemStatus;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.dto.response.MyClosetResponseDTO;
import com.laplace.api.common.dto.response.SellClosetResponseDTO;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.constants.RequestParams;
import com.laplace.api.web.service.WMCClosetService;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.CLOSET)
public class ClosetController {

  private final WMCClosetService WMCClosetService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public ClosetController(WMCClosetService WMCClosetService,
      AuthenticationFacade authenticationFacade) {
    this.WMCClosetService = WMCClosetService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping(APIEndPoints.MY_CLOSET)
  public BaseResponse getMyCloset(@RequestParam(value = RequestParams.ITEM_STATUS, required = false)
      Set<ItemStatus> itemStatuses, Pageable pageable) {
    Integer userId = authenticationFacade.getUserId();

    PageableResponseDTO<MyClosetResponseDTO> myCloset = WMCClosetService
        .getMyCloset(pageable, userId, itemStatuses);
    return BaseResponse.create(myCloset);
  }


  @GetMapping(APIEndPoints.SELL_CLOSET)
  public BaseResponse getSellCloset(
      @RequestParam(value = RequestParams.CUMULATIVE_STATUS, required = false)
          Set<CumulativeItemStatus> cumulativeItemStatuses, Pageable pageable) {
    Integer userId = authenticationFacade.getUserId();

    PageableResponseDTO<SellClosetResponseDTO> myCloset = WMCClosetService
        .getSellCloset(pageable, userId, cumulativeItemStatuses);
    return BaseResponse.create(myCloset);
  }


  @GetMapping(APIEndPoints.FAVORITE_CLOSET)
  public BaseResponse getFavoriteCloset(Pageable pageable) {
    Integer userId = authenticationFacade.getUserId();

    PageableResponseDTO<ItemBasicResponseDto> myCloset = WMCClosetService
        .getFavoriteCloset(userId, pageable);
    return BaseResponse.create(myCloset);
  }
}
