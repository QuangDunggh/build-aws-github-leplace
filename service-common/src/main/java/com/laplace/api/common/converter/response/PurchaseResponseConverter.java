package com.laplace.api.common.converter.response;

import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.dto.response.MyClosetResponseDTO;
import com.laplace.api.common.dto.response.PaymentInfoResponseDTO;
import com.laplace.api.common.dto.response.PurchaseResponseDTO;
import com.laplace.api.common.dto.response.ShippingInfoResponseDTO;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.pay.jp.StripeService;
import com.laplace.api.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PurchaseResponseConverter implements Converter<Order, PurchaseResponseDTO> {

  private final AddressConverter addressConverter;
  private final StripeService stripeService;

  @Autowired
  public PurchaseResponseConverter(
      AddressConverter addressConverter, StripeService stripeService) {
    this.addressConverter = addressConverter;
    this.stripeService = stripeService;
  }

  @Override
  public PurchaseResponseDTO convert(Order order) {
    Item item = order.getItem();
    return PurchaseResponseDTO.builder()
        .deliverySlipNumber(item.getDeliverySlipNumber())
        .purchaseTime(DateUtil.toEpochMilli(order.getCreatedAt()))
        .itemInfo(MyClosetResponseDTO.from(item, StringUtils.EMPTY_STRING))
        .paymentInfo(PaymentInfoResponseDTO
            .from(order, stripeService.getPaymentMethodInfo(order.getPaymentMethodId())))
        .shippingInfo(ShippingInfoResponseDTO.from(order))
        .address(addressConverter.convert(order.getAddress()))
        .build();
  }
}
