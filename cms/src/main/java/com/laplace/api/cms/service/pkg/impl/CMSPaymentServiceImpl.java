package com.laplace.api.cms.service.pkg.impl;

import com.laplace.api.cms.core.dto.PaymentResponseDto;
import com.laplace.api.cms.core.dto.TransactionResponseDTO;
import com.laplace.api.cms.service.pkg.CMSPaymentService;
import com.laplace.api.cms.service.pkg.OrderService;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.repository.db.OrderRepository;
import com.laplace.api.common.repository.db.PaymentRepository;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.util.DateUtil;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class CMSPaymentServiceImpl implements CMSPaymentService {

  public final PaymentRepository paymentRepository;
  public final OrderRepository orderRepository;
  public final ItemService itemService;
  public final OrderService orderService;

  @Autowired
  public CMSPaymentServiceImpl(PaymentRepository paymentRepository,
      OrderRepository orderRepository, ItemService itemService,
      OrderService orderService) {
    this.paymentRepository = paymentRepository;
    this.orderRepository = orderRepository;
    this.itemService = itemService;
    this.orderService = orderService;
  }

  @Override
  public TransactionResponseDTO getAllPayments(Integer userId, Integer month, Integer year,
      Pageable pageable) {
    ZonedDateTime startDate = DateUtil.getFirstDayOfMonth(year, month);
    ZonedDateTime endDate = DateUtil.getLastDayOfMonth(year, month);

    TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
    List<PaymentResponseDto> paymentResponseDtoList = new ArrayList<>();

    Page<Payment> payments = (!ObjectUtils.isEmpty(userId)) ? paymentRepository
        .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startDate, endDate, pageable)
        : paymentRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate, pageable);
    Map<String, Item> itemMap = orderService
        .findItemByIds(payments.stream().map(Payment::getOrderId).collect(Collectors.toSet()));

    payments.forEach(payment -> {
      Item item = itemMap.get(payment.getOrderId());
      transactionResponseDTO
          .setSumOfPrices(transactionResponseDTO.getSumOfPrices() + payment.getOriginalAmount());
      paymentResponseDtoList.add(PaymentResponseDto.from(payment, item));
    });
    transactionResponseDTO.setTotal(payments.getTotalElements());
    transactionResponseDTO.setTotalPage(payments.getTotalPages());
    transactionResponseDTO.setPaymentList(paymentResponseDtoList);
    return transactionResponseDTO;
  }
}
