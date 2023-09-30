package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.enums.DeliveryType;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.constants.enums.OrderStatus;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

import com.laplace.api.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "orders_laplace")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Order {

  @Id
  @Column(name = "order_id")
  private String orderId;

  @Column(name = "item_id")
  private String itemId;

  @Column(name = "seller_id")
  private Integer sellerId;

  @Column(name = "order_code")
  private String orderCode;

  @Column(name = "shipping_name")
  private String shippingName;

  @Column(name = "shipping_name_katakana")
  private String shippingKatakanaName;

  @Column(name = "shipping_email")
  private String shippingEmail;

  @Column(name = "shipping_phone_number")
  private String shippingPhone;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "shipping_address_id")
  private Integer shippingAddressId;

  @Column(name = "receiving_method")
  private Integer receivingMethod;

  @Column(name = "order_amount")
  private Integer orderAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "order_status")
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_status")
  private DeliveryType deliveryType;

  @Column(name = "paymentMethodId")
  private String paymentMethodId;

  @Column(name = "coupon")
  private String coupon;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "order_id", insertable = false, updatable = false, referencedColumnName = "order_id")
  @NotAudited
  private Set<Payment> payments = new LinkedHashSet<>();

  @NotAudited
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", insertable = false, updatable = false, referencedColumnName = "item_id")
  private Item item;

  @NotAudited
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_address_id", insertable = false, updatable = false, referencedColumnName = "id")
  private Address address;

  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Column(name = "confirmed")
  private Boolean confirmed;

  @Enumerated(EnumType.STRING)
  @Column(name = "gift_wrapping_options")
  private GiftWrappingType giftWrappingOptions;

  public static Order create() {
    Order order = new Order();
    order.setOrderId(DateUtil.getUniqueTimeBasedUUID());
    return order;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order that = (Order) o;
    return Objects.equals(orderId, that.orderId) &&
        Objects.equals(sellerId, that.sellerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, sellerId);
  }
}