package com.laplace.api.cms.controller.pkg;


import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.constants.CmsApplicationConstants.RequestParams;
import com.laplace.api.cms.service.pkg.OrderService;
import com.laplace.api.common.constants.enums.EntityType;
import com.laplace.api.common.constants.enums.TypeOfUser;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.marker.AccessControl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static com.laplace.api.common.constants.ApplicationConstants.*;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.PKG_ORDER)
public class CMSOrderController {

  private final OrderService orderService;

  @Autowired
  public CMSOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse getOrders(
      @RequestParam(RequestParams.MONTH) @Range(min = MIN_MONTH, max = MAX_MONTH) Integer month,
      @RequestParam(RequestParams.YEAR) @Min(MIN_YEAR) Integer year,
      @RequestParam(value = RequestParams.USER_ID, required = false) Integer userId,
      @RequestParam(value = RequestParams.TYPE_OF_USER, required = false) TypeOfUser typeOfUser,
      @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
    return BaseResponse
        .create(orderService.getAllOrders(month, year, userId, typeOfUser, pageable));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  @AccessControl(EntityType.PACKAGE_ORDER)
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse getOrderDetails(@PathVariable("id") String orderId) {
    return BaseResponse.create(orderService.getOrderDetailsById(orderId));
  }
}