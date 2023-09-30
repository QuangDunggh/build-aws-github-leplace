package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.response.OrderResponseDto;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class OrderConverter implements Converter<Order, OrderResponseDto> {

  private final PaymentRefsConverter paymentRefsConverter;
  private final UserInfoService userInfoService;
  private final AppUserService appUserService;
  private final UserInfoConverter userInfoConverter;

  @Autowired
  public OrderConverter(PaymentRefsConverter paymentRefsConverter,
      UserInfoConverter userInfoConverter,
      UserInfoService userInfoService,
      AppUserService appUserService) {
    this.paymentRefsConverter = paymentRefsConverter;
    this.userInfoService = userInfoService;
    this.userInfoConverter = userInfoConverter;
    this.appUserService = appUserService;
  }

  @Override
  public OrderResponseDto convert(@Nonnull Order order) {
    return OrderResponseDto.builder()
        .orderId(order.getOrderId())
        .build();
  }
}
