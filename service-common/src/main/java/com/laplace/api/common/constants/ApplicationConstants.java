package com.laplace.api.common.constants;

import com.laplace.api.common.util.DateUtil;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

public class ApplicationConstants {

  public static final String ROLE_PREFIX = "ROLE_";
  public static final String CATEGORY_PREFIX = "category:";
  public static final String VALID_EMAIL_ADDRESS_REGEX = "^.+@[^-\\.].*\\.[a-z]{2,}$";
  public static final String VALID_PHONE_REGEX = "\\d{8,20}";
  public static final String VALID_PHONE_REGEX_JAPANESE = "^[0-9]+$";
  public static final String VALID_PASSWORD_REGEX = "[a-zA-Z0-9|~|!|@|#|$|%|^|&|?]{8,}$";
  public static final String VALID_IMAGE_URL_REGEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
  public static final String VALID_VIDEO_URL_REGEX = "^.*\\.(avi|AVI|wmv|WMV|flv|FLV|mpg|MPG|mp4|MP4)$";
  public static final String VALID_LP_PAGE_NAME = "^[a-zA-Z0-9_-]+( [a-zA-Z0-9_-]+)*$";
  public static final String VALID_COMPANY_NAME = "^[a-zA-Z0-9_-]+( [a-zA-Z0-9_-]+)*$";
  public static final String REQUEST_ID = "requestId";
  public static final Long ZERO = 0L;
  public static final Integer ONE = 1;
  public static final long ONE_LONG = 1;
  public static final Integer TWENTY = 20;
  public static final Long DEFAULT_OFFSET = 10L;
  public static final Long MAXIMUM_OFFSET = 1000L;
  public static final Double ZERO_DOUBLE_VALUE = 0.0;
  public static final String REGEX_ALL = " * ";
  public static final String ALL = "*";
  public static final long ONE_DAY_IN_MILLI = 24L * 3600 * 1000;
  public static final long ONE_HOUR_IN_MILLIS = 3600L * 1000;
  public static final long MAX_LOCK_TIME_IN_SECONDS = 60;
  public static final int VALUE_ZERO = 0;
  public static final int VALUE_ONE = 1;
  public static final int VALUE_NEGATIVE_ONE = -1;
  public static final int HUNDRED = 100;
  public static final int PERCENTAGE = 100;
  public static final int ADDED_YEAR_IN_DATE = 1900;
  public static final int MIN_MONTH = 1;
  public static final int MAX_MONTH = 12;
  public static final int MIN_YEAR = 1970;
  public static final int PROCESSING_FEE_PERCENTAGE = 10;
  public static final int MINIMUM_PURCHASE_PRICE = 5000;
  public static final long MAXIMUM_PURCHASE_PRICE = 100000000;
  public static final int ESTIMATED_DELIVERY_DAYS = 5;
  public static final int MOBILE_MIN_LENGTH = 8;
  public static final int MOBILE_MAX_LENGTH = 13;
  public static final int ITEM_PACKAGE_MAX_SIZE = 5;
  public static final String JP_COUNTRY_CODE = "+81";
  public static final int JP_COUNTRY_CODE_LENGTH = 3;
  public static final Integer JP_ZIP_CODE_PREFIX_LENGTH = 2;
  public static final Integer JP_ZIP_CODE_LENGTH = 7;
  public static final String LANGUAGE = "lan";
  public static final String ACCEPT_LANGUAGE = "Accept-Language";
  public static final String ENGLISH_LANG = "en";
  public static final String JAPANESE_LANGUAGE = "ja";
  public static final String OK_MSG = "OK";
  public static final String SUCCESS_CODE = "200";
  public static final String CREATED_SUCCESS_CODE = "201";
  public static final String CREATED_MSG = "Created";
  public static final String UPDATED_MSG = "Updated";
  public static final HashMap<Integer, String> mailSubject = new HashMap<Integer, String>() {{
    put(MailType.CMS_RESET_PASSWORD, "cms.forgot.password.email.subject");
    put(MailType.WMC_RESET_PASSWORD, "forgotpassword.email.subject");
    put(MailType.CMS_INVITE_USER, "registration.email.subject");
    put(MailType.WMC_ACTIVE_USER, "wmc.registration.email.subject");
    put(MailType.WMC_POST_INQUIRY, "post.inquiry.email.subject");
    put(MailType.WMC_POST_INQUIRY_ADMIN, "post.inquiry.super.admin.email.subject");
    put(MailType.PASSWORD_RESET_SUCCESS, "password.reset.email.subject");
    put(MailType.CMS_GOODS_PUBLISH_REQUEST, "cms.goods.publish.request.email.subject");
    put(MailType.CMS_GOODS_PUBLISH_REQUEST_ACCEPTED_REJECTED,
        "cms.goods.publish.request.accepted.rejected.email.subject");
    put(MailType.CMS_CONTENTS_BORROW_REQUEST, "cms.contents.borrow.request.email.subject");
    put(MailType.CMS_CONTENTS_BORROW_REQUEST_ACCEPTED_REJECTED,
        "cms.contents.borrow.request.accepted.rejected.email.subject");
    put(MailType.CMS_GOODS_PUBLISH_REQUEST_ACTIVATED,
        "cms.goods.publish.request.activated.email.subject");
    put(MailType.CMS_GOODS_PUBLISH_REQUEST_DEACTIVATED,
        "cms.goods.publish.request.deactivated.email.subject");
    put(MailType.CMS_CONTENTS_PUBLISH_REQUEST_CANCELLED,
        "cms.contents.borrow.request.cancelled.email.subject");
    put(MailType.CMS_GOODS_PUBLISH_REQUEST_CANCELLED,
        "cms.goods.publish.request.cancelled.email.subject");
    put(MailType.CMS_AFFILIATE_MOVIES_PUBLISH_REQUEST,
        "cms.affiliate.movies.publish.request.email.subject");
    put(MailType.WMC_CONFIRM_ORDER, "wmc.order.confirm.email.subject");
    put(MailType.CMS_FAKE_ITEM, "cms.fake.item.email.subject");
    put(MailType.WMC_ACCOUNT_WITHDRAW, "wmc.account.withdraw.email.subject");
    put(MailType.PAYMENT_PROBLEM, "payment.problem.email.subject");
    put(MailType.ITEM_PURCHASED_BUYER, "item.purchased.buyer.email.subject");
    put(MailType.ITEM_PURCHASED_SELLER, "item.purchased.seller.email.subject");
    put(MailType.ITEM_CANCELED, "item.cancel.email.subject");
    put(MailType.ITEM_ON_SALE, "item.on.sale.email.subject");
    put(MailType.ITEM_READY_TO_DELIVERED, "item.ready.to.delivered.email.subject");
    put(MailType.ITEM_RETURN_COMPLETED, "item.return.completed.email.subject");
    put(MailType.WMC_RESET_EMAIL, "wmc.reset.email.subject");
    put(MailType.WMC_ID_VERIFICATION_FAILED, "wmc.id.verification.failed.email.subject");
    put(MailType.WMC_ID_VERIFICATION_SUCCESS, "wmc.id.verification.success.email.subject");
    put(MailType.ITEM_DISPLAY_TIME_EXCEEDED, "wmc.item.display.time.exceeded.email.subject");
    put(MailType.ITEM_PRICE_NEGOTIATION, "wmc.item.price.negotiation.email.subject");
    put(MailType.WMC_ACCOUNT_WITHDRAW_ADMIN, "wmc.account.withdraw.admin.email.subject");
  }};
  public static final String EMAIL_URL_TOKEN_PARAMETER = "?token=";
  public static final String EMAIL_URL_EMAIL_PARAMETER = "&email=";
  public static final String EMAIL_URL_ROLE_PARAMETER = "&role=";
  public static final String TEMP_DIR = "/tmp";
  public static final String APPLICATION_LOCAL_PROFILE = "local";
  public static final String APPLICATION_DEV_PROFILE = "dev";
  public static final Integer CANCEL_POLICY = 0;
  public static final Integer DELAY_POLICY = 1;
  public static final String SUPER_ADMIN_USER_ID = "SUPER_ADMIN_USER_ID";
  public static final String SUCCESS = "Success";
  public static final Date EPOCH_START_DATE = new Date(1970, 0, 1);
  public static final String VIDEO_MP4 = "video/mp4";
  public static final Integer NEGATIVE_ONE = -1;
  public static final double VALUE_HUNDRED = 100.00;
  public static final String AFFILIATE_LP_TAGS_SEPARATOR = "\\;";
  public static final String DUPLICATE_ENTRY = "Duplicate entry";
  public static final Integer IMAGE_FILE_SIZE_BYTE = 10_00_000;
  public static final ZonedDateTime CURRENT_ZDT_UTC = DateUtil.timeNow();
  public static final int MAX_ATTEMPT = 2;
  public static final long ONE_HOUR_IN_SECONDS = 3600L;
  public static final long EIGHT_HOUR_IN_SECONDS = 28800L;
  public static final String LAPLACE_API_ACCESS_KEY = "http-x-laplace-api-key";
  public static final String LAPLACE_LAMBDA_ACCESS_KEY = "http-x-laplace-lambda-key";
  public static final String ONLY_DIGIT = "\\d+";
  public static final String PACKAGE_TOKEN_ACCESS_ERROR = "You don't have permission to access";
  public static final String ID = "id";
  public static final long FILE_SIZE = 2L;
  public static final Integer CATEGORY_PARENT_ID = -1;
  public static final String JP_LANG = "jp";
  public static final String FAVORITE_COUNT = "favoriteCount";
  public static final String ORDER_ID = "orderId";
  public static final Integer ANONYMOUS_USER = -1;
  public static final Integer LAMBDA_USER = -101;
  public static final String AUTH_HEADER_NAME = "Authorization";
  public static final String CREATED_AT = "createdAt";
  public static final String CREATED_ON = "createdOn";
  public static final String PUBLISHED_AT = "publishedAt";
  public static final String PICKUP_AT = "pickUpAt";
  public static final String JUDGEMENT_STATUS = "judgementStatus";
  public static final Integer PAYMENT_PARENT_REF_ID = -1;
  public static final int LAST_HOUR = 23;
  public static final int LAST_MIN = 59;
  public static final int LAST_SEC = 59;
  public static final int LAST_NANO_SEC = 999999999;
  public static final int OVER_SALE_MONTH = 6;
  public static final String AUTHORITY_SELLER = "hasRole('ROLE_SELLER')";
  public static final String AUTHORITY_APP_USER = "hasAnyRole('ROLE_USER', 'ROLE_SELLER')";
  public static final String AUTHORITY_ADMIN = "hasRole('ROLE_ADMIN')";
  public static final String ONE_STRING = "1";
  public static String payJPKey = "pk_test_a5ec1d4c799d4c91635caef9";
  public static Integer INITIAL_PAGE_SIZE = 0;
  public static Integer RELATED_CONTENTS_MAX_LIMIT = 4;
  public static Integer TAX_RATE = 4;
  public static String PLUS_REGEX = "\\+";

  private ApplicationConstants() {
  }

  public static ZonedDateTime getCurrentTime() {
    return DateUtil.timeNow();
  }

  public static final class StringUtils {

    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String EQUAL = "=";
    public static final String SEMI_COLON = ";";
    public static final String LESS_EQUAL = "<=";
    public static final String BACK_SLASH = "/";
    public static final String HYPHEN = "-";
    public static final String UNDER_SCORE = "_";
    public static final String WHITESPACE_SEQUENCE = "\\s+";
    public static final String ASTERISK = "*";
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_JSON = "{}";
    public static final String DOT = ".";
    public static final String BAR = "|";
    public static final String SPACE = " ";
    public static final String PLUS = "+";

    private StringUtils() {
    }
  }

  public final class SortOrder {

    public static final String ASC = " asc";
    public static final String DESC = " desc";

    private SortOrder() {
    }
  }

  public final class MailType {

    public static final int CMS_RESET_PASSWORD = 1;
    public static final int CMS_INVITE_USER = 2;
    public static final int WMC_ACTIVE_USER = 3;
    public static final int WMC_RESET_PASSWORD = 4;
    public static final int WMC_POST_INQUIRY_ADMIN = 5;
    public static final int PASSWORD_RESET_SUCCESS = 6;
    public static final int WMC_POST_INQUIRY = 7;
    public static final int CMS_GOODS_PUBLISH_REQUEST = 8;
    public static final int CMS_GOODS_PUBLISH_REQUEST_ACCEPTED_REJECTED = 9;
    public static final int CMS_CONTENTS_BORROW_REQUEST = 10;
    public static final int CMS_CONTENTS_BORROW_REQUEST_ACCEPTED_REJECTED = 11;
    public static final int CMS_GOODS_PUBLISH_REQUEST_ACTIVATED = 12;
    public static final int CMS_GOODS_PUBLISH_REQUEST_DEACTIVATED = 13;
    public static final int CMS_CONTENTS_PUBLISH_REQUEST_CANCELLED = 14;
    public static final int CMS_GOODS_PUBLISH_REQUEST_CANCELLED = 15;
    public static final int CMS_AFFILIATE_MOVIES_PUBLISH_REQUEST = 16;
    public static final int WMC_CONFIRM_ORDER = 17;
    public static final int CMS_FAKE_ITEM = 18;
    public static final int WMC_ACCOUNT_WITHDRAW = 19;
    public static final int PAYMENT_PROBLEM = 20;
    public static final int ITEM_PURCHASED_BUYER = 21;
    public static final int ITEM_PURCHASED_SELLER = 22;
    public static final int ITEM_CANCELED = 23;
    public static final int ITEM_ON_SALE = 24;
    public static final int ITEM_READY_TO_DELIVERED = 25;
    public static final int ITEM_RETURN_COMPLETED = 26;
    public static final int WMC_RESET_EMAIL = 27;
    public static final int WMC_ID_VERIFICATION_FAILED = 28;
    public static final int WMC_ID_VERIFICATION_SUCCESS = 29;
    public static final int ITEM_DISPLAY_TIME_EXCEEDED = 30;
    public static final int ITEM_PRICE_NEGOTIATION = 31;
    public static final int WMC_ACCOUNT_WITHDRAW_ADMIN = 32;
  }

  public final class MailTemplateFields {

    public static final String LINK = "link";
    public static final String SUBJECT = "subject";
    public static final String CODE = "code";
    public static final String USER_NAME = "userName";
    public static final String EMAIL_DOMAIN = "emailDomain";
    public static final String EMAIL = "email";
    public static final String SERVICE_NAME = "serviceName";
    public static final String ROOT_URL = "rootUrl";
    public static final String CONTACT_URL = "contactUrl";
    public static final String ITEM_URL = "itemUrl";
    public static final String REQUEST_TIME = "requestTime";
    public static final String REQUEST_ID = "requestId";
    public static final String ORDER_ID = "orderId";
    public static final String ITEM_ID = "itemId";
    public static final String BRAND_NAME = "brandName";
    public static final String ITEM_NAME = "itemName";
    public static final String SELLER_NAME = "sellerName";
    public static final String BUYER_NAME = "buyerName";
    public static final String ITEM_PRICE = "itemPrice";
    public static final String ORDER_DATETIME = "orderDatetime";
    public static final String DELIVERY_FEES_BEARER = "deliveryFeesBearer";
    public static final String ESTIMATED_DATETIME_OF_ARRIVAL = "estimatedDatetimeOfArrival";
    public static final String PAYMENT_METHOD = "paymentMethod";
    public static final String PAYMENT_AMOUNT = "paymentAmount";
    public static final String RECIPIENT_NAME = "recipientName";
    public static final String RECIPIENT_ADDRESS = "recipientAddress";
    public static final String ESTIMATED_DELIVERY_DAYS = "estimatedDeliveryDays";
    public static final String DEPOSITED_AMOUNT = "depositedAmount";
    public static final String WRAPPING = "wrapping";
    public static final String DELIVERY_TRACKING_NUMBER = "deliveryTrackingNumber";
    public static final String DELIVERY_STATUS_CONFIRMATION_PAGE_URL = "deliveryStatusConfirmationPageUrl";
    public static final String SUPPORT_MAIL = "supportMail";
    public static final String SELL_REQUEST_URL = "sellRequestUrl";
    public static final String NEGOTIATOR_NAME = "negotiatorName";
    public static final String CURRENT_PRICE = "currentPrice";
    public static final String DISCOUNT_REQUEST_PRICE = "discountRequestPrice";
    public static final String SELL_CLOSET_URL = "sellClosetUrl";
    public static final String WITHDRAWAL_REASONS = "withdrawalReasons";
    public static final String DETAILS_REASON = "detailsReason";
  }

  public final class InquiryMailTemplateFields {

    public static final String NAME = "name";
    public static final String CONTENT = "content";
    public static final String INQUIRY_TYPE = "inquiryType";
    public static final String SUPER_ADMIN = "superAdminName";
  }

  public final class ContentType {

    public static final int GOODS = 1;
    public static final int COORDINATES = 2;
  }

  public final class NotificationType {

    public static final int GOODS = 1;
    public static final int COORDINATES = 2;
    public static final int MOVIE = 3;
  }

  public final class DisplayType {

    public static final int RENTAL = 1;
    public static final int PURCHASE = 2;
  }

  public final class PasswordLength {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 32;
  }

  public final class VerificationTokenType {

    public static final int ADMIN_INVITATION = 1;
    public static final int RESET_PASSWORD = 2;
    public static final int EMAIL_OTP = 3;
    public static final int UPDATE_EMAIL_OTP = 4;
  }

  public final class Resources {

    public static final String LOGO = "logo";
    public static final String FAV_ICON = "favIcon";
    public static final String PAY_JP_KEY = "payJPKey";
  }

  public final class EmailSubject {

    public static final String PUBLISH_REQUEST = "Publish request";
    public static final String BORROW_REQUEST = "Borrow request";
  }

  public final class Region {

    public static final String HOKKAIDO = "hokkaido";
    public static final String OKINAWA = "okinawa";
    public static final String ISLAND = "island";
  }

  public final class MyPage {

    public static final String SELL_CLOSET = "sellCloset";
    public static final String SOLD_OUT = "soldOut";
    public static final String PAYMENT_HISTORY = "paymentHistory";
    public static final String LISTING_REQUEST = "listingRequest";
  }
}
