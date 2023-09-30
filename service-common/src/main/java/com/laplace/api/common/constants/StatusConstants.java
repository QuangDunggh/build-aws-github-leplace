package com.laplace.api.common.constants;

import com.laplace.api.common.constants.enums.DeliveryType;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.OrderStatus;
import java.util.Set;

public class StatusConstants {

  public static final Set<ItemStatus> DISPLAYABLE_STATUSES = Set
      .of(ItemStatus.ON_SALE);

  public static final Set<String> TOP_PAGE_DISPLAYABLE_STATUSES_STRING = Set
      .of(ItemStatus.ON_SALE.name(), ItemStatus.PREPARE_TO_SEND_TO_BUYER.name(),
          ItemStatus.ON_THE_WAY_TO_BUYER.name(), ItemStatus.TRANSACTION_COMPLETE.name());

  public static final Set<ItemStatus> RETURN_TO_SELLER_STATES = Set
      .of(ItemStatus.PREPARE_TO_SEND_TO_SELLER,
          ItemStatus.ON_HOLD_PERIOD_EXCEEDED, ItemStatus.ON_SALE_OVER_SIX_MONTH_EXCEEDED);

  public static final Set<ItemStatus> ON_THE_WAY_STATES = Set
      .of(ItemStatus.ON_THE_WAY_TO_BUYER, ItemStatus.ON_THE_WAY_TO_SELLER);

  public static final Set<String> SELLING_STATUSES_STRING = Set
      .of(ItemStatus.ON_SALE.name());

  public static final Set<String> SOLD_OUT_STATUSES_STRING = Set
      .of(ItemStatus.PREPARE_TO_SEND_TO_BUYER.name(), ItemStatus.ON_THE_WAY_TO_BUYER.name(),
          ItemStatus.TRANSACTION_COMPLETE.name());

  public static final Set<ItemStatus> SOLD_OUT_STATUSES = Set
      .of(ItemStatus.PREPARE_TO_SEND_TO_BUYER, ItemStatus.ON_THE_WAY_TO_BUYER,
          ItemStatus.TRANSACTION_COMPLETE);

  public static final Set<ItemStatus> CANCEL_ITEMS_STATUSES = Set
      .of(ItemStatus.ON_SALE_OVER_SIX_MONTH_EXCEEDED,
          ItemStatus.PREPARE_TO_SEND_TO_SELLER, ItemStatus.ON_THE_WAY_TO_SELLER,
          ItemStatus.DISPLAY_CANCELLED, ItemStatus.ON_HOLD, ItemStatus.ON_HOLD_PERIOD_EXCEEDED);

  public static final Set<ItemStatus> MY_CLOSET_ITEMS_STATUSES = Set
      .of(ItemStatus.WAITING_FOR_ARRIVAL, ItemStatus.ITEM_INSPECTION);

  public static final Set<ItemStatus> SELL_CLOSET_ITEMS_STATUSES = Set
      .of(ItemStatus.ON_SALE, ItemStatus.PREPARE_TO_SEND_TO_BUYER, ItemStatus.ON_THE_WAY_TO_BUYER,
          ItemStatus.TRANSACTION_COMPLETE);

  public static final Set<ItemStatus> PRE_ON_SALE_STATUSES = Set
      .of(ItemStatus.WAITING_FOR_ARRIVAL, ItemStatus.ITEM_INSPECTION, ItemStatus.ON_SALE);

  public static final Set<ItemStatus> SELLING_CONDITION_STATES = Set
      .of(ItemStatus.DEFINE_DISPLAY_DATE, ItemStatus.DISPLAY_DATE_RESERVED);

  public static final Set<String> SOLD_OR_CANCELED_STATUSES = Set
      .of(ItemStatus.TRANSACTION_COMPLETE.name(), ItemStatus.DISPLAY_CANCELLED.name());

  public static final Set<DeliveryType> ON_THE_WAY_ORDER_STATES_STRING = Set
      .of(DeliveryType.ON_THE_WAY_TO_BUYER, DeliveryType.ON_THE_WAY_TO_SELLER);

  public static final Set<ItemStatus> DELIVERABLE_STATES = Set
      .of(ItemStatus.ON_THE_WAY_TO_BUYER, ItemStatus.ON_THE_WAY_TO_SELLER,
          ItemStatus.PREPARE_TO_SEND_TO_BUYER, ItemStatus.PREPARE_TO_SEND_TO_SELLER,
          ItemStatus.TRANSACTION_COMPLETE, ItemStatus.DISPLAY_CANCELLED);

  public static final Set<ItemStatus> PICK_UP_STATUSES = Set
      .of(ItemStatus.WAITING_FOR_ARRIVAL, ItemStatus.ITEM_INSPECTION, ItemStatus.ON_SALE,
          ItemStatus.PREPARE_TO_SEND_TO_BUYER, ItemStatus.ON_THE_WAY_TO_BUYER,
          ItemStatus.TRANSACTION_COMPLETE);

  public static final Set<OrderStatus> EXPENDITURES_ORDER_STATUSES = Set
      .of(OrderStatus.CANCEL, OrderStatus.RETURN, OrderStatus.EXTEND);

  public static final Set<OrderStatus> DEPOSITS_ORDER_STATUSES = Set
      .of(OrderStatus.PURCHASE);

  public static final Set<OrderStatus> ORDER_CONFIRM_STATUSES = Set.of(OrderStatus.PURCHASE,
      OrderStatus.CANCEL, OrderStatus.RETURN);
}