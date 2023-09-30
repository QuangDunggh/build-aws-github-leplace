package com.laplace.api.web.controller;

import static com.laplace.api.common.constants.ApplicationConstants.MAX_MONTH;
import static com.laplace.api.common.constants.ApplicationConstants.MIN_MONTH;
import static com.laplace.api.common.constants.ApplicationConstants.MIN_YEAR;

import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.constants.RequestParams;
import com.laplace.api.web.service.WMCPaymentHistoryService;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.PAYMENT_HISTORY)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class WMCPaymentHistoryController {

  private final WMCPaymentHistoryService wmcPurchaseService;

  @Autowired
  public WMCPaymentHistoryController(
      WMCPaymentHistoryService wmcPurchaseService) {
    this.wmcPurchaseService = wmcPurchaseService;
  }

  @GetMapping(APIEndPoints.DEPOSITS)
  public BaseResponse findDeposits(@RequestParam(RequestParams.YEAR) @Min(MIN_YEAR) Integer year,
      @RequestParam(RequestParams.MONTH) @Range(min = MIN_MONTH, max = MAX_MONTH) Integer month,
      @PageableDefault(Integer.MAX_VALUE) Pageable pageable) {
    return BaseResponse.create(wmcPurchaseService.findDeposits(year, month, pageable));
  }


  @GetMapping(APIEndPoints.EXPENDITURE)
  public BaseResponse findExpenditures(
      @RequestParam(RequestParams.YEAR) @Min(MIN_YEAR) Integer year,
      @RequestParam(RequestParams.MONTH) @Range(min = MIN_MONTH, max = MAX_MONTH) Integer month,
      @PageableDefault(Integer.MAX_VALUE) Pageable pageable) {
    return BaseResponse.create(wmcPurchaseService.findExpenditures(year, month, pageable));
  }
}
