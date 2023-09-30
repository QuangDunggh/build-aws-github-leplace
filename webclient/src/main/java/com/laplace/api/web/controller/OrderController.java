package com.laplace.api.web.controller;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.core.bean.OrderBean;
import com.laplace.api.web.service.OrderService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping(APIEndPoints.CART)
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/orders")
  public BaseResponse createOrCancelOrder(@Valid @RequestBody OrderBean orderBean) throws StripeException {
    orderBean.setConfirm(false);
    return BaseResponse.builder()
        .responseType(ResponseType.RESULT)
        .message(Collections.singleton(ApplicationConstants.CREATED_MSG))
        .result(orderService.confirmOrderWithPayment(orderBean))
        .code(ApplicationConstants.CREATED_SUCCESS_CODE)
        .build();
  }

  @GetMapping("/orders")
  public BaseResponse getAllOrderByUser(
      @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
    return BaseResponse.builder()
        .message(Collections.singletonList(ApplicationConstants.OK_MSG))
        .responseType(ResponseType.RESULT)
        .result(orderService.getAllOrderByUser(pageable))
        .code(ApplicationConstants.SUCCESS_CODE)
        .build();
  }

  @GetMapping("/orders/{orderId}")
  public BaseResponse getOrderById(@PathVariable String orderId) {
    return BaseResponse.builder()
        .message(Collections.singletonList(ApplicationConstants.OK_MSG))
        .responseType(ResponseType.RESULT)
        .result(orderService.getOrderById(orderId))
        .code(ApplicationConstants.SUCCESS_CODE)
        .build();
  }
}
