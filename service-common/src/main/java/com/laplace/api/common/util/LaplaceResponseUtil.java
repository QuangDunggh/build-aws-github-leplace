package com.laplace.api.common.util;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.exception.LaplaceApplicationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

public class LaplaceResponseUtil {

  private LaplaceResponseUtil() {
  }

  /**
   * throw exception with result code and MDC request id
   *
   * @param authResultCode
   */
  public static LaplaceApplicationException throwApplicationException(
      ResultCodeConstants authResultCode) throws LaplaceApplicationException {
    return throwApplicationException(authResultCode, MDC.get(ApplicationConstants.REQUEST_ID));
  }

  public static LaplaceApplicationException returnApplicationException(
      ResultCodeConstants authResultCode) throws LaplaceApplicationException {
    return throwApplicationException(authResultCode);
  }

  private static LaplaceApplicationException throwApplicationException(
      ResultCodeConstants authResultCode, String requestId) throws LaplaceApplicationException {
    switch (authResultCode) {
      case USER_NOT_EXISTS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.USER_NOT_EXISTS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PASSWORD:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PASSWORD,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PASSWORD_PATTERN:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PASSWORD_PATTERN,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_TOKEN:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_TOKEN,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case MULTIPLE_SAME_EMAIL:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.MULTIPLE_SAME_EMAIL,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case EMAIL_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.EMAIL_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ALREADY_REGISTER:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ALREADY_REGISTERED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case USER_NOT_ACTIVE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.USER_NOT_ACTIVE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case COMPANY_PROFILE_ALREADY_ADDED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.COMPANY_PROFILE_ALREADY_ADDED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case UNAUTHORIZED_OPERATION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.UNAUTHORIZED_OPERATION,
            HttpStatus.UNAUTHORIZED,
            requestId
        );

      case TAG_NAME_ALREADY_EXISTS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.TAG_NAME_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case TAG_NAME_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.TAG_NAME_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case PAGE_ACCESS_DENIED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.PAGE_ACCESS_DENIED,
            HttpStatus.FORBIDDEN,
            requestId
        );

      case PAGE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.PAGE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case COMPANY_LP_IS_ALREADY_PUBLISHED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.COMPANY_LP_IS_ALREADY_PUBLISHED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case COMPANY_LP_IS_ALREADY_PRIVATE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.COMPANY_LP_IS_ALREADY_PRIVATE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case DEFAULT_META_TAG_MODIFICATION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.DEFAULT_META_TAG_DELETION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_CSV_FORMAT:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_CSV_FORMAT,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PAGE_SECTION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PAGE_SECTION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case STATIC_PAGE_DELETION_FAILED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.STATIC_PAGE_DELETION_FAILED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PAGE_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PAGE_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case CLUSTER_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CLUSTER_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case INVALID_ARGUMENT:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_ARGUMENT,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case CATEGORY_NAME_ALREADY_EXISTS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CATEGORY_NAME_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case CATEGORY_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CATEGORY_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case CATEGORY_IS_PARENT_CATEGORY:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CATEGORY_IS_PARENT_CATEGORY,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case BORROW_CONTENT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.BORROW_CONTENT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case INVALID_REQUEST_STATUS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_REQUEST_STATUS,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case OCCASION_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.OCCASION_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case MULTI_PART_FILE_CONVERSION_FAILED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.MULTI_PART_FILE_CONVERSION_FAILED,
            HttpStatus.INTERNAL_SERVER_ERROR,
            requestId
        );

      case FILE_UPLOAD_FAILED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.FILE_UPLOAD_FAILED,
            HttpStatus.INTERNAL_SERVER_ERROR,
            requestId
        );

      case UNSUPPORTED_IMAGE_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.UNSUPPORTED_IMAGE_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case PUBLISH_REQUEST_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.PUBLISH_REQUEST_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PUBLISH_REQUEST_STATUS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PUBLISH_REQUEST_STATUS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_TIME:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_TIME,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ALREADY_REQUESTED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ALREADY_REQUESTED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case PRODUCT_ID_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.PRODUCT_ID_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case COORDINATE_ID_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.COORDINATE_ID_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_GOODS_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_GOODS_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_COORDINATE_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_COORDINATE_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_REQUEST_ACTIVE_STATUS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_REQUEST_ACTIVE_STATUS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case OLD_PASSWORD_NOT_MATCHED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.OLD_PASSWORD_NOT_MATCHED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case GOODS_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.GOODS_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case MOVIE_ID_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.MOVIE_ID_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PUBLISH_REQUEST_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PUBLISH_REQUEST_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case CONTENT_ACTIVATION_STATUS_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CONTENT_ACTIVATION_STATUS_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ASSIGN_CONTENT_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ASSIGN_CONTENT_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PACKAGE_ID:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PACKAGE_ID,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_CONTENT_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_CONTENT_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_OPERATION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_OPERATION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case UNSUPPORTED_VIDEO_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.UNSUPPORTED_VIDEO_TYPE,
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            requestId
        );

      case LANDING_PAGE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.LANDING_PAGE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case ACCESS_KEY_REQUIRED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ACCESS_KEY_REQUIRED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_ACCESS_KEY:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_ACCESS_KEY,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ALREADY_USED_EMAIL:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ALREADY_USED_EMAIL,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_EMAIL_REGEX:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_EMAIL_PATTERN,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case AUTH_FAILURE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.AUTHENTICATION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case COMPANY_INFO_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.COMPANY_INFO_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_PROFESSION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PROFESSION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case NOTHING_TO_UPDATE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NOTHING_TO_UPDATE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case DUPLICATE_ENTRY:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.DUPLICATE_ENTRY,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case INVALID_NAME_PATTERN:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_NAME_PATTERN,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case WRONG_SNS_TYPE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.WRONG_SNS_TYPE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case TWITTER_EXCEPTION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.TWITTER_EXCEPTION,
            HttpStatus.SERVICE_UNAVAILABLE,
            requestId
        );

      case NOTIFICATION_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NOTIFICATION_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case STORE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.STORE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case ADDRESS_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ADDRESS_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case HOTEL_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.HOTEL_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case CUSTOMER_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CUSTOMER_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );
      case SETUP_INTENT_CREATION_FAILED:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.SETUP_INTENT_CREATION_FAILED,
                HttpStatus.PAYMENT_REQUIRED,
                requestId
        );
      case INVALID_PAYMENT_INTENT_PROVIDED:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_PAYMENT_INTENT_PROVIDED,
                HttpStatus.PAYMENT_REQUIRED,
                requestId
        );
      case ACTIVATION_FAILURE:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.ACTIVATION_FAILURE,
                HttpStatus.BAD_REQUEST,
                requestId
        );
      case STRIPE_CREDENTIALS_ERROR:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.STRIPE_CREDENTIALS_ERROR,
                HttpStatus.PAYMENT_REQUIRED,
                requestId
        );
      case MINIMUM_ONE_CARD_NEEDED:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.MINIMUM_ONE_CARD_NEEDED,
                HttpStatus.PAYMENT_REQUIRED,
                requestId
        );
      case CUSTOMER_CARD_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CUSTOMER_CARD_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );
      case USER_PROFILE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.USER_PROFILE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            requestId
        );
      case USER_PROFILE_NOT_EXISTS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.USER_PROFILE_NOT_EXISTS,
            HttpStatus.NOT_FOUND,
            requestId
        );
      case INVALID_ORDER_REQUEST:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_ORDER_REQUEST,
            HttpStatus.NOT_FOUND,
            requestId
        );
      case OPERATION_TYPE_UNDEFINED:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.OPERATION_TYPE_UNDEFINED,
                HttpStatus.BAD_REQUEST,
                requestId
        );
      case ITEM_ON_WAY_VALIDATION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_ON_WAY_VALIDATION,
            HttpStatus.BAD_REQUEST,
            requestId
        );
      case DUPLICATE_SNS_PROFILE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.DUPLICATE_SNS_PROFILE,
            HttpStatus.BAD_REQUEST,
            requestId
        );
      case IMAGE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.IMAGE_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );
      case INVALID_CANCEL_REQUEST:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_CANCEL_REQUEST,
                HttpStatus.NOT_FOUND,
                requestId
        );
      case INVALID_RETURN_REQUEST:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_CANCEL_REQUEST,
                HttpStatus.NOT_FOUND,
                requestId
        );
      case INVALID_EXTEND_REQUEST:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_EXTEND_REQUEST,
                HttpStatus.NOT_FOUND,
                requestId
        );
      case ITEM_ALREADY_SOLD:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.ITEM_ALREADY_SOLD,
                HttpStatus.BAD_REQUEST,
                requestId
        );
      case INVALID_USER_ID_IN_CANCEL_REQUEST:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_USERID_IN_CANCEL_REQUEST,
                HttpStatus.BAD_REQUEST,
                requestId
        );
      case INVALID_USER_ID_IN_RETURN_REQUEST:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_USERID_IN_RETURN_REQUEST,
                HttpStatus.BAD_REQUEST,
                requestId
        );
      case PAYMENT_CARD_ERROR:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.PAYMENT_CARD_ERROR,
                HttpStatus.BAD_GATEWAY,
                requestId
        );
      case INVALID_SELLER_ID_IN_PURCHASE_REQUEST:
        throw new LaplaceApplicationException(
                authResultCode,
                ErrorCode.INVALID_SELLER_ID_IN_PURCHASE_REQUEST,
                HttpStatus.BAD_REQUEST,
                requestId
        );
      case PAYMENT_NOT_COMPLETED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.PAYMENT_NOT_COMPLETED,
            HttpStatus.BAD_REQUEST,
            requestId
        );
      case ORDER_NOT_EXISTS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ORDER_NOT_EXISTS,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case NAME_TOO_LONG:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NAME_TOO_LONG,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case IMAGE_FILE_SIZE_EXCEEDS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.IMAGE_FILE_SIZE_EXCEEDS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case FAILED_PLATFORM_PAYMENT_REQUEST:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.FAILED_PLATFORM_PAYMENT_REQUEST,
            HttpStatus.NOT_FOUND,
            requestId
        );

      case INVALID_PHONE_NUMBER:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.INVALID_PHONE_NUMBER,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case H1_TAG_TOO_LONG:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.H1_TAG_TOO_LONG,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case CONTENT_TOO_LONG:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.CONTENT_TOO_LONG,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ALREADY_CAPTURED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ALREADY_CAPTURED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case USER_FREEZE:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.USER_FREEZE,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case FILE_UPLOAD_PRE_SIGNED_URL_GENERATE_FAILED:
        return new LaplaceApplicationException(
            authResultCode,
            ErrorCode.FILE_UPLOAD_PRE_SIGNED_URL_GENERATE_FAILED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case TYPE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.TYPE_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ITEM_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case RECEIPT_IMAGE_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.RECEIPT_IMAGE_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case BRAND_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.BRAND_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case EMAIL_NOT_SENT:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.EMAIL_NOT_SENT,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case WRONG_TARGET_COMBINATION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.WRONG_TARGET_COMBINATION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ALREADY_FAKE_DETECTED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ALREADY_FAKE_DETECTED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case NO_VALUE_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NO_VALUE_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case NOT_PERMITTED_STATUS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NOT_PERMITTED_STATUS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case RETURN_ADDRESS_MISSING:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.RETURN_ADDRESS_MISSING,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ITEM_ON_SALE_VALIDATION:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_ON_SALE_VALIDATION,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ACCOUNT_WITHDRAWN:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ACCOUNT_WITHDRAWN,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case DUPLICATE_PACKAGE_ID:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.DUPLICATE_PACKAGE_ID,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case REQUIRED_ONE_ADDRESS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.REQUIRED_ONE_ADDRESS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case BLACKLISTED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.BLACKLISTED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case GIFT_WRAPPING_OPTION_NOT_FOUND:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.GIFT_WRAPPING_OPTION_NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case NOTIFICATION_ALREADY_SENT:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.NOTIFICATION_ALREADY_SENT,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case JSON_CREATION_ERROR:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.JSON_CREATION_ERROR,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ITEM_NAME_SIZE_EXCEEDED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_NAME_SIZE_EXCEEDED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ITEM_DESCRIPTION_SIZE_EXCEEDED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_DESCRIPTION_SIZE_EXCEEDED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ITEM_DIMENSIONS_SIZE_EXCEEDED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_DIMENSIONS_SIZE_EXCEEDED,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      case ITEM_DESIGNER_TIME_SIZE_EXCEEDED:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.ITEM_DESIGNER_TIME_SIZE_EXCEEDED,
            HttpStatus.BAD_REQUEST,
            requestId
        );


      case BRAND_NAME_ALREADY_EXISTS:
        throw new LaplaceApplicationException(
            authResultCode,
            ErrorCode.BRAND_NAME_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST,
            requestId
        );

      default:
        throw LaplaceApplicationException.builder()
            .resultCode(ResultCodeConstants.INTERNAL_SERVER_ERROR)
            .errorCode(ErrorCode.INTERNAL_SERVER_ERROR).requestId(requestId).build();
    }
  }
}
