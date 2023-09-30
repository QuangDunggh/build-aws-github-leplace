package com.laplace.api.web.controller;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.validators.groups.WMCItemInfoValidationGroup;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCItemPackageService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@PreAuthorize(ApplicationConstants.AUTHORITY_SELLER)
@RequestMapping(APIEndPoints.ITEM_PACKAGE)
public class ItemPackageController {

  private final WMCItemPackageService wmcItemPackageService;

  @Autowired
  public ItemPackageController(WMCItemPackageService wmcItemPackageService) {
    this.wmcItemPackageService = wmcItemPackageService;
  }

  @PostMapping
  public BaseResponse createPackage(
      @Validated(WMCItemInfoValidationGroup.class) @Valid @RequestBody ItemPackageRequestDTO itemPackageRequestDTO) {
    return BaseResponse.create(wmcItemPackageService.createPackage(itemPackageRequestDTO));
  }
}
