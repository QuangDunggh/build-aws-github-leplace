package com.laplace.api.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import com.laplace.api.common.util.DateUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
public class ItemBasicResponseDto {

  private String itemName;
  private String descriptions;
  private String itemId;
  private ItemStatus itemStatus;
  private String sellerEmail;
  private Long displayRequestDate;
  private Long listingRequestDate;
  private String image;
  private String brandName;
  private String size;
  private Long likes;
  private String dimensions;
  private Integer displayPrice;
  private Integer prevDisplayPrice;
  private Integer discountPercentage;
  @JsonInclude(Include.NON_NULL)
  private Boolean favorite;

  public static ItemBasicResponseDto from(Item item) {
    return ItemBasicResponseDto.builder()
        .itemName(item.getItemName())
        .descriptions(item.getDescriptions())
        .itemId(item.getItemId())
        .image(item.getCoverImage())
        .itemStatus(item.getStatus())
        .displayPrice(item.getDisplayPrice())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .displayRequestDate(DateUtil.toEpochMilli(item.getDisplayRequestDate()))
        .listingRequestDate(DateUtil.toEpochMilli(item.getCreatedOn()))
        .sellerEmail(item.getSellerEmail())
        .brandName(item.getBrandName())
        .size(item.getSize())
        .likes(item.getFavoriteCount())
        .dimensions(item.getDimensions())
        .discountPercentage(item.getDiscountPercentage())
        .build();

  }

  public static ItemBasicResponseDto from(ItemDocument item, Boolean favorite) {
    return ItemBasicResponseDto.builder()
        .itemName(item.getItemName())
        .descriptions(item.getDescriptions())
        .itemId(item.getId())
        .image(item.getCoverImage())
        .itemStatus(ItemStatus.forName(item.getStatus()))
        .displayPrice(item.getDisplayPrice())
        .prevDisplayPrice(item.getPrevDisplayPrice())
        .discountPercentage(item.getDiscountPercentage())
        .displayRequestDate(item.getDisplayRequestDate())
        .listingRequestDate(item.getCreatedOn())
        .sellerEmail(item.getSellerEmail())
        .brandName(item.getBrandName())
        .size(item.getSize())
        .likes(item.getFavoriteCount())
        .dimensions(item.getDimensions())
        .favorite(favorite)
        .build();
  }

  public static ItemBasicResponseDto from(Item item, boolean favorite) {
    ItemBasicResponseDto itemBasicResponseDto = from(item);
    itemBasicResponseDto.setFavorite(favorite);
    return itemBasicResponseDto;
  }
}
