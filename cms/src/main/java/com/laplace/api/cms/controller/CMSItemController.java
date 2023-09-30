package com.laplace.api.cms.controller;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.constants.CmsApplicationConstants.RequestParams;
import com.laplace.api.cms.service.CMSItemService;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.ItemImageDTO;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.ItemPickUpAndHiddenRequestDto;
import com.laplace.api.common.dto.request.ItemRequestDto;
import com.laplace.api.common.dto.request.ItemStatusChangeRequestDto;
import com.laplace.api.common.util.BaseResponse;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(APIEndPoints.CMS_ITEM)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CMSItemController {

  private final CMSItemService cmsItemService;

  @Autowired
  public CMSItemController(CMSItemService cmsItemService) {
    this.cmsItemService = cmsItemService;
  }

  @PutMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse editItem(@PathVariable("id") String id,
      @Valid @RequestBody ItemRequestDto itemRequestDto) {
    cmsItemService.updateItem(id, itemRequestDto);
    return BaseResponse
        .create(ApplicationConstants.OK_MSG, ApplicationConstants.CREATED_SUCCESS_CODE,
            ApplicationConstants.UPDATED_MSG);
  }

  @PostMapping(APIEndPoints.ITEM_IMAGE)
  public BaseResponse uploadItemImages(@PathVariable("id") String id,
      @Valid @RequestBody Set<ItemImageDTO> imageDTOS) {
    cmsItemService.uploadItemImages(id, imageDTOS);
    return BaseResponse
        .create(ApplicationConstants.OK_MSG, ApplicationConstants.CREATED_SUCCESS_CODE,
            ApplicationConstants.UPDATED_MSG);
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse findItem(@PathVariable("id") String id) {
    return BaseResponse.create(cmsItemService.getItemDetails(id));
  }

  @GetMapping()
  public BaseResponse findAllItem(
      @RequestParam(value = RequestParams.KEYWORD, required = false) String keyword,
      @RequestParam(value = RequestParams.STATUS, required = false) List<ItemStatus> status,
      Pageable pageable) {
    return BaseResponse.create(cmsItemService.getItemList(keyword, status, pageable));
  }

  @PostMapping(APIEndPoints.SEARCH)
  public BaseResponse findAllItem(@Valid @RequestBody ItemSearchRequest itemSearchRequest) {
    return BaseResponse.create(cmsItemService.searchItems(itemSearchRequest));
  }

  @PatchMapping(APIEndPoints.CHANGE_ITEM_STATUS)
  public BaseResponse verifyAndChangeStatus(@PathVariable("id") String id,
      @RequestParam(value = RequestParams.ITEM_STATUS, required = false) ItemStatus status,
      @RequestParam(value = RequestParams.JUDGEMENT_RESULT, required = false) JudgementStatus judgementStatus,
      @RequestParam(value = RequestParams.PACKAGE_ID, required = false) String packageId,
      @RequestParam(value = RequestParams.EXPECTED_DATETIME, required = false) Long expectedDateTime) {
    if (ObjectUtils.isEmpty(status) && ObjectUtils.isEmpty(judgementStatus)) {
      throw throwApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
    }
    ItemStatusChangeRequestDto requestDto = ItemStatusChangeRequestDto.builder()
        .itemId(id)
        .judgementStatus(judgementStatus)
        .status(status)
        .packageId(packageId)
        .expectedDateTime(expectedDateTime)
        .build();
    return BaseResponse.create(cmsItemService.validateAndUpdateStatus(requestDto),
        ApplicationConstants.CREATED_SUCCESS_CODE, ApplicationConstants.UPDATED_MSG);
  }

  @PatchMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse updatePickUpAndHiddenStatus(@PathVariable("id") String id,
      @RequestParam(value = RequestParams.IS_PICK_UP, required = false) Boolean isPickUp,
      @RequestParam(value = RequestParams.IS_HIDDEN, required = false) Boolean isHidden
  ) {
    if ((ObjectUtils.isEmpty(isPickUp) && ObjectUtils.isEmpty(isHidden)) ||
        (!ObjectUtils.isEmpty(isPickUp) && !ObjectUtils.isEmpty(isHidden))) {
      throw throwApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
    }
    ItemPickUpAndHiddenRequestDto requestDto = ItemPickUpAndHiddenRequestDto.builder()
        .itemId(id)
        .isHidden(isHidden)
        .isPicKup(isPickUp)
        .build();
    return BaseResponse.create(cmsItemService.updatePickUpAndHiddenStatus(requestDto),
        ApplicationConstants.CREATED_SUCCESS_CODE, ApplicationConstants.UPDATED_MSG);
  }

  @PatchMapping(APIEndPoints.ITEM_SYNC)
  public BaseResponse sync() {
    cmsItemService.syncItems();
    return BaseResponse.create();
  }
}
