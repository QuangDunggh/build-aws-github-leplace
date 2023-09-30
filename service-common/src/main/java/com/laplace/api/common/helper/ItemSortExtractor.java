package com.laplace.api.common.helper;

import com.laplace.api.common.constants.SearchConstants.SortFields;
import com.laplace.api.common.constants.enums.SearchSortType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ItemSortExtractor {

  private ItemSortExtractor() {
  }

  public static Pageable prepareItemSearchPage(Integer page, Integer size,
      SearchSortType sortType) {
    List<Sort.Order> orders = new ArrayList<>();

    switch (sortType) {
      case PRICE_HIGH_TO_LOW:
        orders.add(Sort.Order.desc(SortFields.DISPLAY_PRICE));
        break;
      case PRICE_LOW_TO_HIGH:
        orders.add(Sort.Order.asc(SortFields.DISPLAY_PRICE));
        break;
      case POPULAR:
        orders.add(Sort.Order.desc(SortFields.FAVORITE_COUNT));
        break;
      case LISTING_DATE_DESC:
        orders.add(Sort.Order.desc(SortFields.LISTING_REQUEST_DATE));
        break;
      case LISTING_DATE_ASC:
        orders.add(Sort.Order.asc(SortFields.LISTING_REQUEST_DATE));
        break;
      case PICK_UP_DATE_DESC:
        orders.add(Sort.Order.desc(SortFields.ESTIMATED_PICK_UP_DATE));
        break;
      case PICK_UP_DATE_ASC:
        orders.add(Sort.Order.asc(SortFields.ESTIMATED_PICK_UP_DATE));
        break;
      case DISPLAY_DATE_DESC:
        orders.add(Sort.Order.desc(SortFields.DISPLAY_REQUEST_DATE));
        break;
      case DISPLAY_DATE_ASC:
        orders.add(Sort.Order.asc(SortFields.DISPLAY_REQUEST_DATE));
        break;
    }
    orders.add(Sort.Order.desc(SortFields.PUBLISHED_AT));
    return PageRequest.of(page, size, Sort.by(orders));
  }

}
