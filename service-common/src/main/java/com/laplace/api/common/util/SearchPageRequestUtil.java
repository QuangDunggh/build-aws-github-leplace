package com.laplace.api.common.util;

import com.laplace.api.common.constants.SearchConstants;
import com.laplace.api.common.constants.SearchConstants.SortFields;
import com.laplace.api.common.constants.enums.SearchSortType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class SearchPageRequestUtil {

  public static Pageable makeRequest(Integer page, Integer size, SearchSortType sortType) {
    List<Sort.Order> orders = new ArrayList<>();

    switch (sortType) {
      case PRICE_HIGH_TO_LOW:
        orders.add(Sort.Order.desc(SearchConstants.SortFields.DISPLAY_PRICE));
        break;
      case PRICE_LOW_TO_HIGH:
        orders.add(Sort.Order.asc(SortFields.DISPLAY_PRICE));
        break;
      default:
        orders.add(Sort.Order.desc(SortFields.CREATED_ON));
    }
    return PageRequest.of(page, size, Sort.by(orders));
  }
}
