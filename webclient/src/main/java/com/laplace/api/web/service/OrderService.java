package com.laplace.api.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.MyPage;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.*;
import com.laplace.api.common.converter.response.OrderConverter;
import com.laplace.api.common.converter.response.PageableResponseConverter;
import com.laplace.api.common.dto.business.NotificationDto;
import com.laplace.api.common.dto.notification.ItemReturnNotificationDTO;
import com.laplace.api.common.dto.notification.ItemSoldNotificationDTO;
import com.laplace.api.common.dto.response.AddressDto;
import com.laplace.api.common.dto.response.OrderResponseDto;
import com.laplace.api.common.dto.response.PaymentMethodResponseDTO;
import com.laplace.api.common.model.db.*;
import com.laplace.api.common.pay.jp.CustomerService;
import com.laplace.api.common.repository.db.OrderRepository;
import com.laplace.api.common.service.*;
import com.laplace.api.common.service.cache.ZipCodeCacheService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.core.bean.OrderBean;
import com.laplace.api.web.core.enums.OrderType;
import com.stripe.exception.StripeException;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.laplace.api.common.constants.ApplicationConstants.LAPLACE_LAMBDA_ACCESS_KEY;
import static com.laplace.api.common.constants.ApplicationConstants.ORDER_ID;
import static com.laplace.api.common.constants.StatusConstants.DEPOSITS_ORDER_STATUSES;
import static com.laplace.api.common.constants.StatusConstants.EXPENDITURES_ORDER_STATUSES;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Slf4j
@Service
public class OrderService {

  private final AuthenticationFacade authenticationFacade;
  private final OrderRepository orderRepository;
  private final WMCPaymentService paymentService;
  private final OrderConverter orderConverter;
  private final WMCEmailService emailService;
  private final ItemService itemService;
  private final AddressService addressService;
  private final WMCAddressService wmcAddressService;
  private final AppUserService appUserService;
  private final UserInfoService userInfoService;
  private final InitialSettingsService initialSettingsService;
  private final CustomerService customerService;
  private final NotificationService notificationService;
  private final MailConfiguration mailConfiguration;
  private final ObjectMapper objectMapper;
  private final ZipCodeCacheService zipCodeCacheService;

  @Autowired
  public OrderService(OrderRepository orderRepository,
      AuthenticationFacade authenticationFacade,
      WMCPaymentService paymentService,
      OrderConverter orderConverter,
      WMCEmailService emailService,
      ItemService itemService,
      AddressService addressService,
      WMCAddressService wmcAddressService,
      AppUserService appUserService,
      UserInfoService userInfoService,
      InitialSettingsService initialSettingsService,
      CustomerService customerService,
      NotificationService notificationService,
      MailConfiguration mailConfiguration,
      ObjectMapper objectMapper,
      ZipCodeCacheService zipCodeCacheService) {
    this.orderRepository = orderRepository;
    this.authenticationFacade = authenticationFacade;
    this.paymentService = paymentService;
    this.orderConverter = orderConverter;
    this.emailService = emailService;
    this.itemService = itemService;
    this.addressService = addressService;
    this.wmcAddressService = wmcAddressService;
    this.appUserService = appUserService;
    this.userInfoService = userInfoService;
    this.initialSettingsService = initialSettingsService;
    this.customerService = customerService;
    this.notificationService = notificationService;
    this.mailConfiguration = mailConfiguration;
    this.objectMapper = objectMapper;
    this.zipCodeCacheService = zipCodeCacheService;
  }

  /**
   * Create order with payment information
   */
  @Transactional
  public Pair<String, String> confirmOrderWithPayment(OrderBean orderBean) throws StripeException {

    Item item = validateOrderInformation(orderBean, getUserId());

    String userEmail = authenticationFacade.getUserContext().get().getEmail();

    return Pair.of(ORDER_ID, placeOrderInformation(orderBean, getUserId(), userEmail, item));
  }

  String placeOrderInformation(OrderBean orderBean, Integer userId, String userEmail, Item item) throws StripeException {
    Order order = confirmOrderWithPayment(item, orderBean, userId);
    switch (orderBean.getType()){
      case RETURN:
        if(order.getConfirmed()){
          sendOrderPaymentEmailAndNotification(order, item, userEmail, orderBean.getType());
          updateCountInformation(orderBean, item);
        }
        break;
      case CREATE:
      case CANCEL:
        sendOrderPaymentEmailAndNotification(order, item, userEmail, orderBean.getType());
        updateCountInformation(orderBean, item);
        break;
      case EXTEND:
        item.setExtendPeriod((null == item.getExtendPeriod() ? 0 : item.getExtendPeriod())
                + orderBean.getExtendPeriod());
        itemService.save(item);
        break;
    }
    return order.getOrderId();
  }

  private void updateCountInformation(OrderBean orderBean, Item item) {
    try {
      if (orderBean.getType() == OrderType.CREATE) {
        appUserService.updateCountOnPurchase(item.getSellerId());
      } else {
        appUserService.decrementItemsOnDisplayCount(item.getSellerId());
      }
    } catch (Exception ex) {
      log.error("User item purchase/display count increment/decrement failed " + ex.getLocalizedMessage());
    }
  }

  private Order confirmOrderWithPayment(Item item, OrderBean orderBean, Integer userId) throws StripeException {

    //return previous order if already set to return
    if (orderBean.getType() == OrderType.RETURN) {
      Order order = orderRepository.findByItemIdAndUserIdAndStatusAndConfirmedFalse(item.getItemId(), userId, OrderStatus.RETURN)
              .orElse(null);
      if (null != order && !orderBean.isConfirm()) {
        return order;
      }
    }

    Order order = Order.create();
    orderBean.setOrderId(order.getOrderId());
    order.setUserId(userId);
    order.setSellerId(item.getSellerId());
    order.setCreatedAt(DateUtil.timeNow().withZoneSameInstant(ZoneId.of("Asia/Tokyo")));
    order.setOrderCode(order.getOrderId());
    order.setPaymentMethodId(orderBean.getPaymentMethodId());
    order.setItemId(orderBean.getItemId());
    setOrderStatusInfo(order, orderBean);
    Payment payment = paymentService.confirmPaymentFromOrder(item, orderBean, order);
    setOrderShippingInfo(order, orderBean);
    order.setOrderAmount(payment.getOriginalAmount());

    try {
      order = orderRepository.save(order);
      order.setPayments(Set.of(payment));
      return order;
    } catch (Exception ex) {
      log.error("Can not create order information: " + order.getOrderId() + " --> " + ex.getLocalizedMessage());
      log.info(order.toString());
      order.setPayments(Set.of(payment));
      return order;
    }
  }

  private void setOrderStatusInfo(Order order, OrderBean orderBean){
    OrderType orderType = orderBean.getType();
    order.setGiftWrappingOptions(GiftWrappingType.NO_WRAPPING);
    switch (orderType){
      case CANCEL:
        order.setStatus(OrderStatus.CANCEL);
        order.setConfirmed(true);
        order.setDeliveryType(DeliveryType.PREPARE_TO_SEND_TO_SELLER);
        break;
      case RETURN:
        order.setConfirmed(orderBean.isConfirm());
        order.setStatus(OrderStatus.RETURN);
        order.setDeliveryType(DeliveryType.PREPARE_TO_SEND_TO_SELLER);
        break;
      case EXTEND:
        order.setConfirmed(true);
        order.setStatus(OrderStatus.EXTEND);
        order.setDeliveryType(DeliveryType.TRANSACTION_COMPLETE);
        break;
      case CREATE:
        order.setConfirmed(true);
        order.setStatus(OrderStatus.PURCHASE);
        order.setDeliveryType(DeliveryType.PREPARE_TO_SEND_TO_BUYER);
        order.setGiftWrappingOptions( orderBean.getGiftWrappingOptions());
        break;
      default:
        break;
    }
  }

  private void setOrderShippingInfo(Order order,OrderBean orderBean){
    order.setShippingName(orderBean.getShippingName());
    order.setShippingKatakanaName(orderBean.getShippingKatakanaName());
    order.setShippingEmail(orderBean.getShippingEmail());
    order.setShippingAddressId(orderBean.getShippingAddressId());
    order.setShippingPhone(orderBean.getShippingPhoneNumber());
  }

  public void sendOrderPaymentEmailAndNotification(Order order, Item item, String email, OrderType type) {
    try {
      switch (type) {
        case CREATE:
          AppUser appUser = appUserService.findById(order.getSellerId())
              .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
          AppUser buyer = appUserService.findById(authenticationFacade.getUserId())
              .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
          notificationService.saveNotification(buildSoldOutNotificationDto(item, order,
              buyer.getAppUserProfile().getUserName()));
          emailService.sendItemPurchasedMailToBuyer(email, order, item, Languages.JAPANESE);
          emailService.sendItemPurchasedMailToSeller(appUser.getEmail(), order, item, Languages.JAPANESE);
          break;
        case CANCEL:
          emailService.sendItemCancelMail(email, order, Languages.JAPANESE);
          break;
        case RETURN:
            notificationService.saveNotification(buildItemDisplayTimeExceededNotificationDto(item));
            emailService.sendItemDisplayTimeExceededMail(email, item, Languages.JAPANESE);
        case EXTEND:
          break;
      }
    } catch (Exception exception) {
      log.error("Order -> Item purchase -> cancel -> email sending failed: " + exception.getLocalizedMessage());
    }
  }

  public PageableResponseDTO<OrderResponseDto> getAllOrderByUser(Pageable pageable) {
    PageableResponseConverter<Order, OrderResponseDto> converter = new PageableResponseConverter<>();
    return converter.convert(orderRepository.findByUserIdAndConfirmedTrue(getUserId(), pageable),
        orderConverter);
  }

  public OrderResponseDto getOrderById(String orderId) {
    Order order = orderRepository.findByOrderIdAndUserIdAndConfirmedTrue(orderId, getUserId())
        .orElseThrow(() -> throwApplicationException(ORDER_NOT_EXISTS));
    return orderConverter.convert(order);
  }

  public Page<Order> getAllOrdersByDeliveryType(Integer userId, Set<DeliveryType> deliveryTypes,
      Pageable request) {
    return orderRepository.findByUserIdAndDeliveryTypeInAndStatus(userId, deliveryTypes,
        OrderStatus.PURCHASE, request);
  }

  public Order findOrder(String orderId) {
    return orderRepository.findByOrderIdAndUserIdAndStatusAndConfirmedTrue(orderId, getUserId(),
            OrderStatus.PURCHASE)
        .orElseThrow(() -> throwApplicationException(ORDER_NOT_EXISTS));
  }

  public Page<Order> getOrderBySellerIdOfMonth(Integer userId, Integer year, Integer month,
      Pageable pageable) {
    return orderRepository
        .findByCreatedAtBetweenAndSellerId(DateUtil.getFirstDayOfMonth(year, month),
            DateUtil.getLastDayOfMonth(year, month), userId, pageable);
  }

  public Page<Order> getOrderByUserIdOfMonth(Integer userId, Integer year, Integer month,
      Pageable pageable) {
    return orderRepository.findByCreatedAtBetweenAndUserId(DateUtil.getFirstDayOfMonth(year, month),
        DateUtil.getLastDayOfMonth(year, month), userId, pageable);
  }

  public Page<Order> getOrderByStatusAndSellerIdByMonth(Integer userId, Integer year, Integer month,
      Set<OrderStatus> statuses, Pageable pageable) {
    return orderRepository.findByCreatedAtBetweenAndSellerIdAndStatusIn(
        DateUtil.getFirstDayOfMonth(year, month), DateUtil.getLastDayOfMonth(year, month), userId,
        statuses, pageable);
  }

  public Page<Order> getExpendituresOrderByMonth(Integer userId, Integer year, Integer month,
      Pageable pageable) {
    return orderRepository.findByCreatedAtBetweenAndUserId(DateUtil.getFirstDayOfMonth(year, month),
        DateUtil.getLastDayOfMonth(year, month), userId, pageable);
  }

  public boolean existsByUserIdAndDeliveryTypeIn(Integer userId, Set<DeliveryType> deliveryTypes) {
    return orderRepository.existsByUserIdAndDeliveryTypeIn(userId, deliveryTypes);
  }

  @Async("taskExecutor")
  @Transactional
  public void returnOnSaleItem(String itemId, String lambdaKey) throws StripeException {
    Item item = itemService.findById(itemId)
            .orElseThrow(() -> returnApplicationException(ITEM_NOT_FOUND));

    //cancel payment intent if exists
    if (!item.getStatus().equals(ItemStatus.ON_SALE)) {
      Order order = orderRepository.findByItemIdAndUserIdAndStatusAndConfirmedFalse(itemId, item.getSellerId(), OrderStatus.RETURN)
              .orElse(null);
      if (null != order) {
        Optional<Payment> result = order.getPayments()
                .stream().parallel()
                .filter(payment -> payment.getPaymentType() == PaymentType.FIX &&
                        payment.getUserId().equals(item.getSellerId())).findAny();
        if (result.isPresent()) {
          paymentService.releaseIfAlreadySold(result.get());
        }
        order.setConfirmed(true);
        orderRepository.save(order);
      }
      throwApplicationException(INVALID_RETURN_REQUEST);
    }

    //Display period not end yet
    if ((item.getStatus().equals(ItemStatus.ON_SALE)) && item.getDisplayRequestDate()
            .plusMonths(getItemDisplayExpiredPeriod(item)).isAfter(DateUtil.timeNow())) {
      throwApplicationException(INVALID_CANCEL_REQUEST);
    }

    MDC.put(LAPLACE_LAMBDA_ACCESS_KEY, lambdaKey);
    Integer sellerId = item.getSellerId();

    AppUser appUser = appUserService.findById(sellerId)
        .orElseThrow(() -> returnApplicationException(USER_NOT_EXISTS));
    AppUserProfile profile = appUser.getAppUserProfile();
    if(ObjectUtils.isEmpty(profile)) {
      throw throwApplicationException(USER_PROFILE_NOT_EXISTS);
    }
    //if user already select card, shipping info for return after display ending
    Order order = orderRepository.findByItemIdAndSellerIdAndConfirmedFalse(itemId, sellerId).orElse(null);

    if (null != order) {
      Optional<Payment> result = order.getPayments().stream().parallel()
              .filter(payment -> payment.getPaymentType() == PaymentType.FIX &&
                      payment.getUserId().equals(item.getSellerId())).findAny();

      if (result.isPresent()) {
        order.setConfirmed(true);
        paymentService.fixPaymentFromOrder(order, item, result.get());
        item.setStatus(ItemStatus.PREPARE_TO_SEND_TO_SELLER);
        orderRepository.save(order);
        itemService.save(item);
        appUserService.decrementItemsOnDisplayCount(item.getSellerId());
        try {
              notificationService.saveNotification(buildItemDisplayTimeExceededNotificationDto(item));
              emailService.sendItemDisplayTimeExceededMail(appUser.getEmail(), item, Languages.JAPANESE);
        } catch (Exception exception) {
          log.error("Order -> return -> email sending failed: " + exception.getLocalizedMessage());
        }
        return;
      }
    }

    //if user does not set any shipping info or payment method use default
    OrderBean orderBean = getOnSaleSystemReturnRequestParams(profile, sellerId, item);
    item.setLockedBy(item.getSellerId());
    item.setOnHoldAt(DateUtil.timeNow());
    itemService.onlyItemSaveInDB(item);
    placeOrderInformation(orderBean, sellerId, orderBean.getShippingEmail(), item);
  }

  protected OrderBean getOnSaleSystemReturnRequestParams(AppUserProfile profile, Integer sellerId, Item item){
    Address address = addressService.getAppUserDefaultAddress(sellerId)
            .orElseThrow(() -> returnApplicationException(ADDRESS_NOT_FOUND));
    String sellerEmail = appUserService
            .findEmailMapByIds(Sets.newHashSet(sellerId))
            .get(sellerId);

    return OrderBean.builder()
        .itemId(item.getItemId())
        .shippingAddressId(address.getId())
        .shippingName(profile.getName())
        .shippingKatakanaName(profile.getKataKanaName())
        .shippingPhoneNumber(profile.getPhoneNumber())
        .shippingEmail(sellerEmail)
        .type(OrderType.RETURN)
        .confirm(true)
        .paymentMethodId(getPaymentMethod(sellerId))
        .deliveryFee(zipCodeCacheService.getDeliveryFee(address.getZip()).getCancelDeliveryFee())
        .build();
  }

  protected String getPaymentMethod(Integer userId) {

    List<PaymentMethodResponseDTO> paymentMethods = customerService.getCustomerPaymentMethods(userId);

    if (paymentMethods.size() < ApplicationConstants.ONE) {
      throw throwApplicationException(CUSTOMER_CARD_NOT_FOUND);
    }

    return paymentMethods.get(ApplicationConstants.VALUE_ZERO).getPaymentMethod();
  }

  private Integer getUserId() {
    return authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(AUTH_FAILURE))
        .getUserId();
  }

  public synchronized Item validateOrderInformation(OrderBean orderBean, Integer userId) throws StripeException {
    Item item = itemService.findById(orderBean.getItemId()).orElseThrow(
            () -> throwApplicationException(ITEM_NOT_FOUND));

    if (null == orderBean.getType()) {
      throw throwApplicationException(OPERATION_TYPE_UNDEFINED);
    }

    if (ObjectUtils.isNotEmpty(item.getLockedBy()) && ObjectUtils.isNotEmpty(item.getOnHoldAt()) &&
            ObjectUtils.notEqual(item.getLockedBy(), userId)
            && Duration.between(item.getOnHoldAt(), DateUtil.timeNow()).getSeconds()
            < ApplicationConstants.MAX_LOCK_TIME_IN_SECONDS) {
      log.info(userId + " -- " + item.getLockedBy() + " -- " + item.getOnHoldAt() + " -- "
              + Duration.between(item.getOnHoldAt(), DateUtil.timeNow()).getSeconds());
      paymentService.refundIfAlreadyPaid(orderBean);
      throw throwApplicationException(ITEM_ALREADY_SOLD);
    }

    switch (orderBean.getType()) {
      case EXTEND:
        if (item.getStatus() != ItemStatus.ON_SALE) {
          throw throwApplicationException(INVALID_EXTEND_REQUEST);
        }
        if (!item.getCreatedBy().equals(userId)) {
          throw throwApplicationException(INVALID_EXTEND_REQUEST);
        }
        if (null == orderBean.getExtendPeriod() || orderBean.getExtendPeriod() < ApplicationConstants.ONE) {
          throw throwApplicationException(INVALID_EXTEND_REQUEST);
        }
        setDefaultShippingInfo(orderBean);
        break;
      case CANCEL:
        orderBean.setDeliveryFee(
            validateShippingAddress(orderBean, userId).getDeliveryInfo().getCancelDeliveryFee());
        if (item.getStatus() != ItemStatus.ON_SALE) {
          throw throwApplicationException(INVALID_CANCEL_REQUEST);
        }
        if (!item.getCreatedBy().equals(userId)) {
          throw throwApplicationException(INVALID_USER_ID_IN_CANCEL_REQUEST);
        }
        break;
      case RETURN:
        orderBean.setDeliveryFee(
            validateShippingAddress(orderBean, userId).getDeliveryInfo().getCancelDeliveryFee());
        if (item.getStatus() != ItemStatus.ON_SALE) {
          throw throwApplicationException(INVALID_RETURN_REQUEST);
        }
        if (!item.getCreatedBy().equals(userId)) {
          throw throwApplicationException(INVALID_USER_ID_IN_RETURN_REQUEST);
        }
        break;
      case CREATE:
        orderBean.setDeliveryFee(
            validateShippingAddress(orderBean, userId).getDeliveryInfo().getDeliveryFee());
        if (item.getStatus() != ItemStatus.ON_SALE) {
          paymentService.refundIfAlreadyPaid(orderBean);
          throw throwApplicationException(ITEM_ALREADY_SOLD);
        }
        if (item.getCreatedBy().equals(userId)) {
          throw throwApplicationException(INVALID_SELLER_ID_IN_PURCHASE_REQUEST);
        }
        if (ObjectUtils.isEmpty(orderBean.getGiftWrappingOptions())) {
          throw throwApplicationException(GIFT_WRAPPING_OPTION_NOT_FOUND);
        }
        break;
    }

    if (ObjectUtils.isEmpty(orderBean.getPaymentMethodId())) {
      throw throwApplicationException(PAYMENT_NOT_FOUND);
    }

    item.setLockedBy(getUserId());
    item.setOnHoldAt(DateUtil.timeNow());
    itemService.onlyItemSaveInDB(item);

    return item;
  }

  private AddressDto validateShippingAddress(OrderBean orderBean, Integer userId){
    AddressDto address = wmcAddressService.getAddressById(orderBean.getShippingAddressId(), userId);

    if (ObjectUtils.isEmpty(address)) {
      throw throwApplicationException(ADDRESS_NOT_FOUND);
    }
    return address;
  }

  private void setDefaultShippingInfo(OrderBean orderBean){
    orderBean.setShippingAddressId(ApplicationConstants.VALUE_NEGATIVE_ONE);
    orderBean.setShippingEmail("no-reply@leclair.co.jp");
    orderBean.setShippingName(ApplicationConstants.StringUtils.EMPTY_STRING);
    orderBean.setShippingKatakanaName(ApplicationConstants.StringUtils.EMPTY_STRING);
    orderBean.setShippingPhoneNumber(ApplicationConstants.StringUtils.EMPTY_STRING);
  }

  private NotificationDto buildSoldOutNotificationDto(Item item, Order order, String userName) {
    return NotificationDto.builder()
        .fromUserId(order.getUserId())
        .userId(order.getSellerId())
        .type(NotificationType.ITEM_SOLD)
        .dataOfMessage(
            ItemSoldNotificationDTO.makeJson(objectMapper, item, userName,
                mailConfiguration.getWmcMyPageUrl() + MyPage.SOLD_OUT,
                mailConfiguration.getWmcMyPageUrl() + MyPage.PAYMENT_HISTORY))
        .itemId(order.getItemId())
        .build();
  }

  private Integer getItemDisplayExpiredPeriod(Item item) {
    return initialSettingsService.getInitialSettings().map(InitialSettings::getDisplayPeriod)
            .orElse(ApplicationConstants.OVER_SALE_MONTH) + (null == item.getExtendPeriod() ? 0 : item.getExtendPeriod());
  }

  private NotificationDto buildItemDisplayTimeExceededNotificationDto(Item item) {
    return NotificationDto.builder()
        .fromUserId(item.getSellerId())
        .userId(item.getSellerId())
        .type(NotificationType.ITEM_RETURN)
        .dataOfMessage(ItemReturnNotificationDTO.makeJson(objectMapper, item,
            mailConfiguration.getWmcMyPageUrl() + MyPage.PAYMENT_HISTORY))
        .itemId(item.getItemId())
        .build();
  }
}
