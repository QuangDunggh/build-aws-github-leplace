package com.laplace.api.web.controller;

import com.laplace.api.common.constants.enums.PurchaseStatus;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.constants.RequestParams;
import com.laplace.api.web.service.WMCPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.PURCHASE)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class PurchaseController {

  private final WMCPurchaseService wmcPurchaseService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  PurchaseController(WMCPurchaseService wmcPurchaseService,
      AuthenticationFacade authenticationFacade) {
    this.wmcPurchaseService = wmcPurchaseService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping
  public BaseResponse findPurchasedItems(
      @RequestParam(RequestParams.PURCHASE_STATUS) PurchaseStatus purchaseStatus,
      Pageable pageable) {
    return BaseResponse.create(wmcPurchaseService.findPurchasedItems(purchaseStatus, pageable));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse findPurchaseDetails(@PathVariable("id") String orderId) {
    return BaseResponse.create(wmcPurchaseService.findPurchaseDetails(orderId));
  }

}
