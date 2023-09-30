package com.laplace.api.web.controller.user;

import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.core.bean.SellerProfileUpdateRequest;
import com.laplace.api.web.service.impl.WMCSellerProfileService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.SELLER)
@Validated
@PreAuthorize("hasRole('ROLE_SELLER')")
public class SellerController {

  private final WMCSellerProfileService sellerProfileService;

  @Autowired
  public SellerController(
      WMCSellerProfileService sellerProfileService) {
    this.sellerProfileService = sellerProfileService;
  }

  @GetMapping(APIEndPoints.PROFILE)
  public BaseResponse updateUserBasicInfo() {
    return BaseResponse.create(sellerProfileService.getProfile());
  }

  @PutMapping(APIEndPoints.PROFILE)
  public BaseResponse updateUserBasicInfo(
      @Valid @RequestBody SellerProfileUpdateRequest request) {
    return BaseResponse.create(sellerProfileService.update(request));
  }

}
