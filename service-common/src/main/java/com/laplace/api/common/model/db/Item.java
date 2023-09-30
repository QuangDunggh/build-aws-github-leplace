package com.laplace.api.common.model.db;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.DBTables;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = DBTables.ITEMS)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
public class Item {

  @Id
  @Column(name = "item_id", nullable = false)
  private String itemId;

  @Column(name = "seller_id", nullable = false)
  private Integer sellerId;

  @Column(name = "seller_email", nullable = false)
  private String sellerEmail;

  @Column(name = "name", nullable = false)
  private String itemName;

  @Column(name = "descriptions", nullable = false)
  private String descriptions;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ItemStatus status;

  @Column(name = "target_audience", nullable = false)
  @Enumerated(EnumType.STRING)
  private TargetAudience targetAudience;

  @Column(name = "category", nullable = false)
  @Enumerated(EnumType.STRING)
  private Category category;

  @Column(name = "sub_category", nullable = false)
  private String subCategory;

  @Column(name = "brand_id", nullable = false)
  private String brandId;

  @Column(name = "designer_time", nullable = false)
  private String designerTime;

  @Column(name = "cover_image")
  private String coverImage;

  @Column(name = "receipt_available", nullable = false)
  private Boolean receiptAvailable;

  @Column(name = "receipt_image")
  private String receiptImage;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemId", orphanRemoval = true)
  private Set<ItemImage> images;

  @Column(name = "display_request_date")
  private ZonedDateTime displayRequestDate;

  @Column(name = "published_at")
  private ZonedDateTime publishedAt;

  @Column(name = "seller_purchase_time")
  @Enumerated(EnumType.STRING)
  private SellerPurchaseTime sellerPurchaseTime;

  @Column(name = "expected_pick_up_date")
  private Long estimatedPickUpTimeByLaplace;

  @Column(name = "color")
  private String color;

  @Column(name = "size")
  private String size;

  @Column(name = "dimensions")
  private String dimensions;

  @Column(name = "display_price")
  @Min(value = ApplicationConstants.MINIMUM_PURCHASE_PRICE, message = ErrorCode.INVALID_ARGUMENT)
  private Integer displayPrice;

  @Column(name = "shipping_price")
  private Integer shippingPrice;

  @Column(name = "cancel_fee")
  private Integer cancelFee;

  @Column(name = "tax_rate")
  private Integer taxRate;

  @Column(name = "delivery_fee_bearer")
  @Enumerated(EnumType.STRING)
  private DeliveryFeeBearer deliveryFeeBearer;

  @Column(name = "physical_condition")
  @Enumerated(EnumType.STRING)
  private PhysicalCondition physicalCondition;

  @Column(name = "seller_purchase_location")
  @Enumerated(EnumType.STRING)
  private SellerPurchaseLocation sellerPurchaseLocation;

  @Column(name = "judgement_status")
  @Enumerated(EnumType.STRING)
  private JudgementStatus judgementStatus;

  @Column(name = "pick_up")
  private Boolean pickUp;

  @Column(name = "pick_up_at")
  private ZonedDateTime pickUpAt;

  @Column(name = "on_hold_at")
  private ZonedDateTime onHoldAt;

  @Column(name = "on_hold_by")
  private Integer lockedBy;

  @Column(name = "extend_period")
  private Integer extendPeriod;

  @Column(name = "hidden")
  private Boolean hidden;

  @Column(name = "favourite_count")
  private Long favoriteCount;

  @Column(name = "seller_comment")
  private String sellerComment;

  @Column(name = "package_id")
  private String packageId;

  @Column(name = "delivery_slip_number")
  private String deliverySlipNumber;

  @Column(name = "address_id")
  private Integer addressId;

  @Column(name = "expected_datetime")
  private ZonedDateTime expectedDateTime;

  @Column(name = "discount_percentage")
  private Integer discountPercentage;

  @Column(name = "blacklisted")
  private Boolean blacklisted;

  @Column(name = "inspection_report")
  private String inspectionReport;

  @Column(name = "prev_display_price")
  private Integer prevDisplayPrice;

  @Column(name = "created_by", nullable = false)
  private Integer createdBy;

  @Column(name = "created_on", nullable = false)
  private ZonedDateTime createdOn;

  @Column(name = "last_updated_by")
  private Integer lastUpdatedBy;

  @Column(name = "last_updated_on")
  private ZonedDateTime lastUpdatedOn;

  public static Item create() {
    Item info = new Item();
    info.setItemId(shortUUID());
    return info;
  }

  private static String shortUUID() {
    UUID uuid = UUID.randomUUID();
    long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
    return Long.toString(l, Character.MAX_RADIX);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item that = (Item) o;
    return Objects.equals(getItemId(), that.getItemId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getItemId());
  }

  @Transient
  public String getBrandName() {
    return ObjectUtils.isEmpty(getBrandId()) ? StringUtils.EMPTY_STRING
        : getBrandId().split(StringUtils.COLON)[ApplicationConstants.VALUE_ZERO];
  }

  public void buildBrandId(Brand brand) {
    setBrandId(brand.getBrandName() + StringUtils.COLON + brand.getBrandId());
  }
}
