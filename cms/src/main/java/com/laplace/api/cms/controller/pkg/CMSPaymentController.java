package com.laplace.api.cms.controller.pkg;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.constants.CmsApplicationConstants.RequestParams;
import com.laplace.api.cms.service.pkg.CMSPaymentService;
import com.laplace.api.common.constants.enums.EntityType;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.marker.AccessControl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static com.laplace.api.common.constants.ApplicationConstants.*;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.PKG_PAYMENT)
public class CMSPaymentController {

  private final CMSPaymentService paymentService;

  @Autowired
  public CMSPaymentController(CMSPaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GetMapping()
  @AccessControl(EntityType.FUJ_HISTORY_PAYMENT)
  public BaseResponse getPayments(
      @RequestParam(RequestParams.MONTH) @Range(min = MIN_MONTH, max = MAX_MONTH) Integer month,
      @RequestParam(RequestParams.YEAR) @Min(MIN_YEAR) Integer year,
      @RequestParam(value = RequestParams.USER_ID, required = false) Integer userId,
      @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
    return BaseResponse.create(paymentService.getAllPayments(userId, month, year, pageable));
  }
}
