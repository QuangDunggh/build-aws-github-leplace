package com.laplace.api.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

  /**
   * Replace an existing sort field with a provided sort field
   *
   * @param pageable
   * @param oldField existing sort field
   * @param newField new sort field
   * @return
   */
  public static PageRequest updateSortField(Pageable pageable, String oldField, String newField) {
    PageRequest pageRequest;
    if (!pageable.getSort().isSorted()) {
      return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    }

    List<Sort.Order> orders = new ArrayList<>();
    Stream<Sort.Order> orderStream = pageable.getSort().get();
    Sort.Order oldOrder = pageable.getSort().getOrderFor(oldField);
    if (oldOrder != null) {
      Sort.Order newOrder = new Sort.Order(oldOrder.getDirection(), newField);
      orders.add(newOrder);

    }
    List<Sort.Order> otherOrders = orderStream
        .filter(order -> !order.getProperty().equals(oldField)).collect(Collectors.toList());
    if (!otherOrders.isEmpty()) {
      orders.addAll(otherOrders);
    }
    Sort sort = Sort.by(orders);
    pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    return pageRequest;
  }

  public static PageRequest updateSortFieldWithoutOtherField(Pageable pageable, String oldField,
      String newField) {
    PageRequest pageRequest;
    if (!pageable.getSort().isSorted()) {
      return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    }

    List<Sort.Order> orders = new ArrayList<>();
    Sort.Order oldOrder = pageable.getSort().getOrderFor(oldField);
    if (oldOrder != null) {
      Sort.Order newOrder = new Sort.Order(oldOrder.getDirection(), newField);
      orders.add(newOrder);
    }

    Sort sort = Sort.by(orders);
    pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    return pageRequest;
  }

}
