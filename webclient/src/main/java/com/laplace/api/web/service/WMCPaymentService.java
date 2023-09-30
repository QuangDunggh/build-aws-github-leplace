package com.laplace.api.web.service;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.constants.enums.PaymentType;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.pay.jp.PaymentService;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.web.core.bean.OrderBean;
import com.stripe.exception.StripeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;
import static com.laplace.api.common.util.PaymentUtil.calculateProcessingFee;

@Service
public class WMCPaymentService {

  private final PaymentService paymentService;

  private final InitialSettingsService initialSettingsService;

  public WMCPaymentService(PaymentService paymentService,
                           InitialSettingsService initialSettingsService) {
    this.paymentService = paymentService;
    this.initialSettingsService = initialSettingsService;
  }


  public Payment confirmPaymentFromOrder(Item item, OrderBean orderBean, Order order) throws StripeException {
    Payment payment = getPaymentObject(item, orderBean, orderBean.getShippingName(), order.getUserId());
    return paymentService.confirmPayment(payment, item, orderBean.getShippingEmail(), order);
  }

  public void refundIfAlreadyPaid(OrderBean orderBean) throws StripeException {
    if (!StringUtils.isBlank(orderBean.getPaymentIntent())) {
      paymentService.fullRefundIfValid(orderBean.getPaymentIntent(), orderBean.getPaymentMethodId());
    }
  }

  public void releaseIfAlreadySold(Payment payment) {
    paymentService.releasePaymentIntent(payment);
  }

  public Payment fixPaymentFromOrder(Order order, Item item, Payment payment) throws StripeException{
    return paymentService.confirmPayment(payment, item, order.getShippingEmail(), order);
  }

  private Payment getPaymentObject(Item item, OrderBean orderBean, String name, Integer userId) {
    InitialSettingsDTO initialSettings = InitialSettingsDTO
            .from(initialSettingsService.getInitialSettings().orElse(null));
    Payment payment = new Payment();
    payment.setChargeId(orderBean.getPaymentIntent());
    payment.setUserId(userId);
    payment.setUserName(name);
    payment.setSellerId(item.getSellerId());
    payment.setOrderId(orderBean.getOrderId());
    payment.setItemOriginalPrice(getPrice(item.getDisplayPrice()));
    payment.setDeliveryFee(getPrice(orderBean.getDeliveryFee()));
    payment.setSellerAmount(VALUE_ZERO);
    payment.setProcessingFee(VALUE_ZERO);
    payment.setGiftWrappingPrice(VALUE_ZERO);
    payment.setPaymentType(PaymentType.PAY);
    payment.setPaymentRefId(ApplicationConstants.CATEGORY_PARENT_ID);
    payment.setPaymentMethod(orderBean.getPaymentMethodId());
    payment.setCreatedAt(DateUtil.timeNow());
    payment.setDescription(item.getItemId() + "::" + userId + "::" + name);
    switch (orderBean.getType()) {
      case CREATE:
        payment.setProcessingFee(calculateProcessingFee(payment.getItemOriginalPrice(),
                initialSettings.getProcessingRate()));
        payment.setGiftWrappingPrice(
                orderBean.getGiftWrappingOptions().equals(GiftWrappingType.NO_WRAPPING) ? VALUE_ZERO
                        : initialSettings.getGiftWrappingFee());
        payment.setOriginalAmount(
                (item.getDeliveryFeeBearer().equals(DeliveryFeeBearer.BUYER)) ? calculateTotalFee(
                        payment.getItemOriginalPrice(), payment.getGiftWrappingPrice(),
                        payment.getDeliveryFee())
                        : calculateTotalFee(payment.getItemOriginalPrice(), payment.getGiftWrappingPrice(),
                        VALUE_ZERO));
        payment.setSellerAmount(calculateSellerAmount(item.getDisplayPrice(),
                (item.getDeliveryFeeBearer().equals(DeliveryFeeBearer.SELLER) ?
                        payment.getDeliveryFee() : VALUE_ZERO), payment.getProcessingFee()));
        break;
      case RETURN:
        payment.setOriginalAmount(calculateTotalCancelFee(VALUE_ZERO,
                payment.getDeliveryFee()));
        break;
      case CANCEL:
        payment.setOriginalAmount(calculateTotalCancelFee(getPrice(initialSettings.getCancelFee()),
                payment.getDeliveryFee()));
        break;
      case EXTEND:
        payment.setOriginalAmount(initialSettings.getDisplayExtensionFee() * orderBean.getExtendPeriod());
        payment.setDeliveryFee(VALUE_ZERO);
        break;
    }
    return payment;
  }

  private int calculateTotalFee(int itemPrice, int giftWrappingFee, int deliveryFee) {
    return itemPrice + giftWrappingFee + deliveryFee;
  }

  private int calculateTotalCancelFee(int cancelFee, int deliveryFee){
    return cancelFee + deliveryFee;
  }

  private int getPrice(Integer price) {
    return null == price ? VALUE_ZERO : price;
  }

  private Integer calculateSellerAmount(int itemPrice, int applicationFee, int deliveryFee){
    return itemPrice - (applicationFee + deliveryFee);
  }
}
