package com.laplace.api.cms.service.pkg.impl;

import com.laplace.api.cms.service.pkg.OrderService;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.TypeOfUser;
import com.laplace.api.common.converter.response.PageableResponseConverter;
import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.dto.response.OrderResponseDetailsDto;
import com.laplace.api.common.dto.response.OrderResponseDto;
import com.laplace.api.common.model.db.*;
import com.laplace.api.common.pay.jp.PaymentService;
import com.laplace.api.common.pay.jp.StripeService;
import com.laplace.api.common.repository.db.OrderRepository;
import com.laplace.api.common.repository.db.PaymentRepository;
import com.laplace.api.common.service.AddressService;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.stripe.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.laplace.api.common.constants.StatusConstants.ORDER_CONFIRM_STATUSES;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.AUTH_FAILURE;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.NOT_FOUND;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;
  private final AuthenticationFacade authenticationFacade;
  private final PaymentService paymentService;
  private final AppUserService appUserService;
  private final StripeService stripeService;
  private final AddressService addressService;
  private final InitialSettingsService initialSettingsService;

  public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository,
      AuthenticationFacade authenticationFacade, PaymentService paymentService,
      AppUserService appUserService, StripeService stripeService,
      AddressService addressService,
      InitialSettingsService initialSettingsService) {
    this.orderRepository = orderRepository;
    this.paymentRepository = paymentRepository;
    this.authenticationFacade = authenticationFacade;
    this.paymentService = paymentService;
    this.appUserService = appUserService;
    this.stripeService = stripeService;
    this.addressService = addressService;
    this.initialSettingsService = initialSettingsService;
  }

  @Override
  public OrderResponseDetailsDto getOrderDetailsById(String orderId) {
    return getOrderById(orderId);
  }

  @Override
  public Optional<Order> getOrderByItemId(String itemId) {
    return orderRepository.findByItemIdAndStatusIn(itemId, ORDER_CONFIRM_STATUSES);
  }

  @Override
  public PageableResponseDTO<OrderResponseDto> getAllOrders(Integer month, Integer year,
      Integer userId, TypeOfUser typeOfUser, Pageable pageable) {
    ZonedDateTime startDate = DateUtil.getFirstDayOfMonth(year, month);
    ZonedDateTime endDate = DateUtil.getLastDayOfMonth(year, month);

    List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
    Page<Order> orders;
    if (!ObjectUtils.isEmpty(userId)) {
      orders = typeOfUser.equals(TypeOfUser.BUYER) ? orderRepository
          .findByCreatedAtBetweenAndUserId(startDate, endDate, userId, pageable)
          : orderRepository.findByCreatedAtBetweenAndSellerIdOrderByCreatedAtDesc(startDate,
              endDate, userId, pageable);
    } else {
      orders = orderRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate,
          pageable);
    }

    Map<Integer, String> sellerEmailMap = appUserService.findEmailMapByIds(orders.stream()
        .map(Order::getSellerId).collect(Collectors.toSet()));

    Map<Integer, String> buyerEmailMap = appUserService.findEmailMapByIds(orders.stream()
        .map(Order::getUserId).collect(Collectors.toSet()));

    orders.forEach(order -> orderResponseDtoList.add(OrderResponseDto
        .make(order, sellerEmailMap.get(order.getSellerId()), buyerEmailMap.get(order.getUserId())))
    );
    PageableResponseConverter<Order, OrderResponseDto> responseConverter = new PageableResponseConverter<>();
    return responseConverter.make(orders, orderResponseDtoList);
  }

  @Override
  public Map<String, Item> findItemByIds(Set<String> orderIds) {
    return orderRepository.findAllByOrderIdIn(orderIds).stream()
        .collect(Collectors.toMap(Order::getOrderId, Order::getItem));
  }

  @Override
  public void updateDeliveryType(String itemId, ItemStatus status) {
    orderRepository.updateDeliveryType(itemId, status.name());
  }

  private OrderResponseDetailsDto getOrderById(String orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    Payment payment = paymentRepository.findPaymentByOrderIdAndPaymentRefId(order.getOrderId(),
            ApplicationConstants.VALUE_NEGATIVE_ONE)
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    PaymentMethod paymentMethod = stripeService.getPaymentMethodInfo(order.getPaymentMethodId());
    AppUser seller = appUserService.findById(order.getSellerId())
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    AppUser buyer = appUserService.findById(order.getUserId())
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    Address address = addressService.getAddressById(order.getShippingAddressId());
    GiftWrappingDTO giftWrappingDTO = initialSettingsService.getGiftWrappingDetails(
        order.getGiftWrappingOptions());
    return OrderResponseDetailsDto.make(order, payment, paymentMethod, seller.getEmail(),
        buyer.getEmail(), address, giftWrappingDTO);
  }

  private Integer getUserId() {
    return authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(AUTH_FAILURE))
        .getUserId();
  }
}
