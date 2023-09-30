package com.laplace.api.web.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.DeliveryType;
import com.laplace.api.common.constants.enums.PurchaseStatus;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.converter.response.PurchaseResponseConverter;
import com.laplace.api.common.dto.response.PurchaseResponseDTO;
import com.laplace.api.common.dto.response.PurchasedItemResponseDTO;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.service.OrderService;
import com.laplace.api.web.service.WMCPurchaseService;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WMCPurchaseServiceImpl implements WMCPurchaseService {

  private final AuthenticationFacade authenticationFacade;
  private final OrderService orderService;
  private final PurchaseResponseConverter purchaseResponseConverter;

  @Autowired
  public WMCPurchaseServiceImpl(
      AuthenticationFacade authenticationFacade,
      OrderService orderService,
      PurchaseResponseConverter purchaseResponseConverter) {
    this.authenticationFacade = authenticationFacade;
    this.orderService = orderService;
    this.purchaseResponseConverter = purchaseResponseConverter;
  }

  @Override
  public PageableResponseDTO<PurchasedItemResponseDTO> findPurchasedItems(
      PurchaseStatus purchaseStatus,
      Pageable page) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.CREATED_AT)));
    Page<Order> orderPage;
    switch (purchaseStatus) {
      case ITEMS_ON_THE_WAY: {
        orderPage = orderService
            .getAllOrdersByDeliveryType(authenticationFacade.getUserId(), Collections.singleton(
                DeliveryType.PREPARE_TO_SEND_TO_BUYER), request);
        break;
      }
      case ITEMS_ALREADY_SENT: {
        orderPage = orderService
            .getAllOrdersByDeliveryType(authenticationFacade.getUserId(), Set.of(
                DeliveryType.ON_THE_WAY_TO_BUYER, DeliveryType.TRANSACTION_COMPLETE), request);
        break;
      }
      default: {
        throw throwApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
      }
    }

    return PageableResponseDTO
        .create(orderPage.getTotalElements(), orderPage.getTotalPages(), orderPage.stream()
            .map(PurchasedItemResponseDTO::from)
            .collect(Collectors.toList()));
  }

  @Override
  public PurchaseResponseDTO findPurchaseDetails(String orderId) {
    return purchaseResponseConverter.convert(orderService.findOrder(orderId));
  }
}
