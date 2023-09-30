package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments_laplace")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "seller_id")
  private Integer sellerId;

  @Column(name = "order_id")
  private String orderId;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "payment_ref_id")
  private Integer paymentRefId;

  @Column(name = "charge_id")
  private String chargeId;

  @Column(name = "description")
  private String description;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "total_amount")
  private Integer originalAmount;

  @Column(name = "refund_amount")
  private Integer refundAmount;

  @Column(name = "item_original_price")
  private Integer itemOriginalPrice;

  @Column(name = "processing_fee")
  private Integer processingFee;

  @Column(name = "shipping_price")
  private Integer deliveryFee;

  @Column(name = "gift_wrapping_price")
  private Integer giftWrappingPrice;

  @Column(name = "actual_refund_amount")
  private Integer actualRefundAmount;

  @Column(name = "stripe_processing_fee")
  private Integer stripeProcessingFee;

  @Column(name = "seller_receiving_amount")
  private Integer sellerAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type")
  private PaymentType paymentType;

  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    Payment that = (Payment) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(sellerId, that.sellerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sellerId);
  }
}