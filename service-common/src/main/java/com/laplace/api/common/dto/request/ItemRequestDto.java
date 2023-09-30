package com.laplace.api.common.dto.request;

import static com.laplace.api.common.constants.ApplicationConstants.MAXIMUM_PURCHASE_PRICE;
import static com.laplace.api.common.constants.ApplicationConstants.MINIMUM_PURCHASE_PRICE;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.TextLength;
import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import com.laplace.api.common.constants.enums.PhysicalCondition;
import com.laplace.api.common.constants.enums.SellerPurchaseLocation;
import com.laplace.api.common.constants.enums.SellerPurchaseTime;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.validators.MustBeNull;
import com.laplace.api.common.validators.groups.WMCItemInfoValidationGroup;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemRequestDto {

  @Size(max = TextLength.ITEM_NAME_MAX, message = ErrorCode.ITEM_NAME_SIZE_EXCEEDED)
  private String name;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT, groups = {WMCItemInfoValidationGroup.class})
  @Size(max = TextLength.ITEM_DESCRIPTION_MAX, message = ErrorCode.ITEM_DESCRIPTION_SIZE_EXCEEDED)
  private String description;
  @MustBeNull(message = ErrorCode.INVALID_ARGUMENT, groups = {WMCItemInfoValidationGroup.class})
  private String inspectionReport;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private TargetAudience targetAudience;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Category category;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer subCategoryIndex;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT)
  private Integer brandId;
  @Size(max = TextLength.ITEM_DESIGNER_TIME_MAX, message = ErrorCode.ITEM_DESIGNER_TIME_SIZE_EXCEEDED)
  private String designerTime;
  private String coverImage;
  private boolean receiptAvailable;
  private String receiptImage;
  private String color;
  private String size;
  @Size(max = TextLength.ITEM_DIMENSIONS_MAX, message = ErrorCode.ITEM_DIMENSIONS_SIZE_EXCEEDED)
  private String dimensions;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT, groups = {WMCItemInfoValidationGroup.class})
  @Range(min = MINIMUM_PURCHASE_PRICE, max = MAXIMUM_PURCHASE_PRICE, message = ErrorCode.INVALID_ARGUMENT)
  private Integer displayPrice;
  private DeliveryFeeBearer deliveryFeeBearer;
  private PhysicalCondition physicalCondition;
  @NotNull(message = ErrorCode.INVALID_ARGUMENT, groups = {WMCItemInfoValidationGroup.class})
  private SellerPurchaseLocation sellerPurchaseLocation;
  private SellerPurchaseTime sellerPurchaseTime;
  private String packageId;
  private Integer addressId;
  @JsonIgnore
  private Integer sellerId;
  @JsonIgnore
  private Integer lastUpdatedBy;
}
