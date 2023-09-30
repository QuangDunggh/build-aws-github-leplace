package com.laplace.api.common.constants;

public final class SearchConstants {

  private SearchConstants() {
  }

  public static final class Analyzer {

    private Analyzer() {
    }

    /**
     * Japanese language analyzer
     */
    public static final String KUROMOJI = "kuromoji";
  }

  public static final class SortFields {


    private SortFields() {
    }

    public static final String CREATED_ON = "createdOn";
    public static final String PUBLISHED_AT = "publishedAt";
    public static final String FAVORITE_COUNT = "favoriteCount";
    public static final String DISPLAY_PRICE = "displayPrice";
    public static final String LISTING_REQUEST_DATE = "createdOn";
    public static final String ESTIMATED_PICK_UP_DATE = "estimatedPickUpTimeByLaplace";
    public static final String DISPLAY_REQUEST_DATE = "displayRequestDate";
  }

  public static final class FilterFields {



    private FilterFields() {
    }

    public static final String KEYWORDS = "keywords";
    public static final String ID = "id";
    public static final String CATEGORY = "category";
    public static final String SUB_CATEGORY = "subCategory";
    public static final String BRAND_ID = "brandId";
    public static final String RECEIPT_AVAILABLE = "receiptAvailable";
    public static final String SELLER_ID = "sellerId";
    public static final String TARGET_AUDIENCE = "targetAudience";
    public static final String STATUS = "status";
    public static final String COLOR = "color";
    public static final String SIZE = "size";
    public static final String DELIVERY_FEE_BEARER = "deliveryFeeBearer";
    public static final String PICK_UP = "pickUp";
    public static final String HIDDEN = "hidden";
    public static final String DISPLAY_PRICE = "displayPrice";
    public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
    public static final String PHYSICAL_CONDITION = "physicalCondition";
    public static final String JUDGEMENT_STATUS = "judgementStatus";
    public static final String DISPLAY_REQUEST_DATE = "displayRequestDate";
    public static final String BLACKLISTED = "blacklisted";
    public static final String SELLER_EMAIL_KEYWORD = "sellerEmail.keyword";
    public static final String SUB_CATEGORY_EN = SUB_CATEGORY+".en";
    public static final String SUB_CATEGORY_JP = SUB_CATEGORY+".jp";
    public static final String BRAND_NAME = "brandName";
    public static final String ITEM_NAME = "itemName";
  }

  public static final class Index {

    private Index() {
    }

    public static final String ITEMS = "items";
  }

  public static final class Mapping {

    private Mapping() {
    }

    public static final String ITEMS = "es-mapping/items-mapping.json";
  }

  public static final class Type {

    private Type() {
    }

    public static final String ITEMS = "itemdocument";
  }
}
