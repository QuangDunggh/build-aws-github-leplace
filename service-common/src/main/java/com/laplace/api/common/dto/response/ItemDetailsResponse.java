package com.laplace.api.common.dto.response;

import static com.laplace.api.common.util.PaymentUtil.calculateProcessingFee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.PhysicalCondition;
import com.laplace.api.common.constants.enums.RemindPeriod;
import com.laplace.api.common.constants.enums.SellerPurchaseLocation;
import com.laplace.api.common.constants.enums.SellerPurchaseTime;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.dto.AppUserProfileDto;
import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.dto.ItemImageDTO;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.ItemImage;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.util.DateUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
@Builder
public class ItemDetailsResponse {

  private String itemId;
  private String itemName;
  private ItemStatus itemStatus;
  private TargetAudience targetOfUse;
  private Category category;
  private String type;
  private String brandId;
  private String brandName;
  private String designerTime;
  private String coverImage;
  private Boolean receiptExistence;
  private String itemReceiptImage;
  private String color;
  private String size;
  private Long likes;
  private String itemSizeDetails;
  private Integer itemPrice;
  private Integer prevDisplayPrice;
  private Integer discountPercentage;
  private Integer processingFee;
  private Integer cancelFee;
  private Integer deliveryFee;
  private Integer giftWrappingFee;
  private DeliveryFeeBearer deliveryFeeBearer;
  private PhysicalCondition physicalCondition;
  private SellerPurchaseLocation sellerPurchaseLocation;
  private SellerPurchaseTime purchaseTime;
  private Integer sellerId;
  private String sellerEmail;
  private Long displayRequestDate;
  private Long listingRequestDate;
  private String sellerComment;
  private String description;
  private String inspectionReport;
  private JudgementStatus judgementStatus;
  private List<ItemImageDTO> displayImages;
  @JsonInclude(Include.NON_NULL)
  private Boolean favorite;
  private Boolean pickUp;
  private Boolean hidden;
  private String deliverySlipNumber;
  private Integer displayPeriod;
  private RemindPeriod remindPeriod;
  private Integer totalPrice;
  private Integer extendPeriod;
  private GiftWrappingDTO giftWrappingInfo;
  private AppUserProfileDto appUserProfileInfo;
  private AddressDto addressInfo;

  public static ItemDetailsResponse from(Item item, String sellerEmail,
      InitialSettingsDTO settings) {
    return ItemDetailsResponse.builder()
        .itemId(item.getItemId())
        .itemName(item.getItemName())
        .itemStatus(item.getStatus())
        .targetOfUse(item.getTargetAudience())
        .category(item.getCategory())
        .type(item.getSubCategory())
        .brandId(getBrandId(item.getBrandId()))
        .brandName(getBrandName(item.getBrandId()))
        .designerTime(item.getDesignerTime())
        .coverImage(item.getCoverImage())
        .receiptExistence(item.getReceiptAvailable())
        .itemReceiptImage(item.getReceiptImage())
        .color(item.getColor())
        .size(item.getSize())
        .likes(item.getFavoriteCount())
        .itemSizeDetails(item.getDimensions())
        .itemPrice(item.getDisplayPrice())
        .deliveryFeeBearer(item.getDeliveryFeeBearer())
        .physicalCondition(item.getPhysicalCondition())
        .sellerPurchaseLocation(item.getSellerPurchaseLocation())
        .purchaseTime(item.getSellerPurchaseTime())
        .sellerId(item.getSellerId())
        .sellerEmail(sellerEmail)
        .displayRequestDate(DateUtil.toEpochMilli(item.getDisplayRequestDate()))
        .listingRequestDate(DateUtil.toEpochMilli(item.getCreatedOn()))
        .sellerComment(item.getSellerComment())
        .description(item.getDescriptions())
        .inspectionReport(item.getInspectionReport())
        .displayImages(createImageDto(item.getImages()))
        .processingFee(calculateProcessingFee(item.getDisplayPrice(), settings.getProcessingRate()))
        .cancelFee(settings.getCancelFee())
        .deliveryFee(settings.getDeliveryFee())
        .giftWrappingFee(settings.getGiftWrappingFee())
        .judgementStatus(item.getJudgementStatus())
        .pickUp(item.getPickUp())
        .hidden(item.getHidden())
        .discountPercentage(item.getDiscountPercentage())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .deliverySlipNumber(item.getDeliverySlipNumber())
        .displayPeriod(settings.getDisplayPeriod())
        .remindPeriod(settings.getRemindPeriod())
        .extendPeriod(item.getExtendPeriod())
        .build();
  }

  public static ItemDetailsResponse from(Item item, String sellerEmail,
      InitialSettingsDTO settings, Address address) {
    ItemDetailsResponse response = from(item, sellerEmail, settings);
    response.setAddressInfo(AddressDto.from(address));
    return response;
  }

  public static ItemDetailsResponse from(Item item, String sellerEmail, InitialSettingsDTO settings,
      AppUserProfile appUserProfile, Address address, GiftWrappingDTO giftWrappingDTO) {
    ItemDetailsResponse response = from(item, sellerEmail, settings, address);
    response.setAppUserProfileInfo(AppUserProfileDto.from(appUserProfile));
    response.setGiftWrappingInfo(giftWrappingDTO);
    return response;
  }

  public static ItemDetailsResponse from(Item item, String sellerEmail,
      InitialSettingsDTO settings, boolean favorite) {
    ItemDetailsResponse response = from(item, sellerEmail, settings);
    response.setFavorite(favorite);
    return response;
  }

  public static ItemDetailsResponse from(Item item, String sellerEmail,
      InitialSettingsDTO settings, Payment payment) {
    ItemDetailsResponse response = from(item, sellerEmail, settings);
    response.setGiftWrappingFee(payment.getGiftWrappingPrice());
    response.setDeliveryFee(payment.getDeliveryFee());
    response.setTotalPrice(payment.getOriginalAmount());
    return response;
  }

  private static List<ItemImageDTO> createImageDto(Set<ItemImage> images) {
    return images.stream().map(image -> ItemImageDTO.builder()
            .imageUrl(image.getImageUrl())
            .isCoverImage(image.isCoverImage())
            .number(image.getNumber())
            .build()
        )
        .sorted(Comparator.comparing(ItemImageDTO::getNumber))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  private static String getBrandName(String brandId) {
    return ObjectUtils.isEmpty(brandId) ? StringUtils.EMPTY_STRING
        : brandId.split(StringUtils.COLON)[ApplicationConstants.VALUE_ZERO];
  }

  @JsonIgnore
  private static String getBrandId(String brandId) {
    return ObjectUtils.isEmpty(brandId) ? StringUtils.EMPTY_STRING
        : brandId.split(StringUtils.COLON)[ApplicationConstants.VALUE_ONE];
  }
}
