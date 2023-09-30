package com.laplace.api.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.PhysicalCondition;
import com.laplace.api.common.constants.enums.SearchSortType;
import com.laplace.api.common.constants.enums.TargetAudience;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchRequest {

  private Integer sellerId;
  private String sellerEmail;
  private String keyword;
  private Set<String> status;
  @JsonIgnore
  private Set<String> notInStatuses;
  @JsonIgnore
  private String notInItemId;
  private TargetAudience target;
  private Category category;
  private Set<String> subCategories;
  private Set<Integer> brands;
  private Set<PhysicalCondition> physicalConditions;
  private JudgementStatus judgementStatus;
  private String itemSize;
  private Boolean hidden;
  private Boolean isNotBlacklisted;
  private Boolean hideInvalidItems;
  private Boolean pickUp;
  private Boolean stock;
  private Boolean priceCut;
  private Boolean soldOut;
  @Builder.Default
  private Long startPrice = ApplicationConstants.ZERO;
  @Builder.Default
  private Long endPrice= ApplicationConstants.MAXIMUM_PURCHASE_PRICE;
  @Builder.Default
  private SearchSortType sort = SearchSortType.RECENT;
  @Builder.Default
  private Integer page = ApplicationConstants.ZERO.intValue();
  @Builder.Default
  private Integer size = ApplicationConstants.DEFAULT_OFFSET.intValue();
  @Builder.Default
  private String lang = Languages.JAPANESE;
  private Boolean newItems;
  private Boolean popularItems;
  private Long startTime;
  private Long endTime;
}
