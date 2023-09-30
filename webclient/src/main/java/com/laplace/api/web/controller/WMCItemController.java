package com.laplace.api.web.controller;


import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.request.SellConditionRequestDto;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCItemService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.ITEMS)
public class WMCItemController {

  private final WMCItemService wmcItemService;

  @Autowired
  public WMCItemController(WMCItemService wmcItemService) {
    this.wmcItemService = wmcItemService;
  }

  @PutMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse updateItem(@PathVariable(ApplicationConstants.ID) String id,
      @Valid @RequestBody SellConditionRequestDto requestDto) {
    wmcItemService.updateItem(id, requestDto);
    return BaseResponse
        .create(ApplicationConstants.OK_MSG, ApplicationConstants.CREATED_SUCCESS_CODE,
            ApplicationConstants.UPDATED_MSG);
  }

}
