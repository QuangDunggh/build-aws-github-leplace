package com.laplace.api.web.service.impl;

import static com.laplace.api.common.constants.StatusConstants.DEPOSITS_ORDER_STATUSES;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.response.DepositResponseDTO;
import com.laplace.api.common.dto.response.ExpendituresResponseDTO;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.service.OrderService;
import com.laplace.api.web.service.WMCPaymentHistoryService;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WMCPaymentHistoryServiceImpl implements WMCPaymentHistoryService {

  private final OrderService orderService;
  private final AuthenticationFacade authenticationFacade;
  private final ItemService itemService;

  @Autowired
  public WMCPaymentHistoryServiceImpl(OrderService orderService,
      AuthenticationFacade authenticationFacade,
      ItemService itemService) {
    this.orderService = orderService;
    this.authenticationFacade = authenticationFacade;
    this.itemService = itemService;
  }

  @Override
  public DepositResponseDTO findDeposits(Integer year, Integer month,
      Pageable page) {

    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.CREATED_AT)));
    Page<Order> orders = orderService.getOrderByStatusAndSellerIdByMonth(
        authenticationFacade.getUserId(), year, month, DEPOSITS_ORDER_STATUSES, request);

    Map<String, Item> itemMapById = itemService
        .findByIds(orders.getContent().stream().map(Order::getItemId).collect(
            Collectors.toSet()));
    return DepositResponseDTO.from(orders.getContent(), itemMapById);
  }

  @Override
  public ExpendituresResponseDTO findExpenditures(Integer year, Integer month, Pageable page) {

    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, ApplicationConstants.CREATED_AT)));
    Page<Order> orders = orderService.getExpendituresOrderByMonth(authenticationFacade.getUserId(),
        year, month, request);

    Map<String, Item> itemMapById = itemService
        .findByIds(orders.getContent().stream().map(Order::getItemId).collect(
            Collectors.toSet()));

    return ExpendituresResponseDTO.from(orders.getContent(), itemMapById);
  }
}
