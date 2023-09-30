package com.laplace.api.common.util;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.PageSection;
import com.laplace.api.common.constants.enums.PageType;
import com.laplace.api.common.constants.enums.PublishRequestType;
import com.laplace.api.common.constants.enums.RequestType;
import com.laplace.api.common.constants.enums.SupportedImageType;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public class ValidationUtil {

  public static boolean hasSectionPermission(PageType pageType, PageSection section) {
    switch (pageType) {
      case HOME:
        switch (section) {
          case H1_TAG:
          case SERVICE_DESCRIPTION:
          case RENTAL_IMAGE:
          case PURCHASE_IMAGE:
          case RENTAL_DESC:
          case PURCHASE_DESC:
          case COORD_DESC:
          case NEWS:
          case STYLIST:
          case GOODS:
            return true;
        }
        break;

      case GOOD_DETAILS:
        switch (section) {
          case STYLIST_REVIEW_DESC:
          case NEWS_DESC:
          case RELATED_COORD_DESC:
          case RELATED_GOODS_DESC:
            return true;
        }
        break;

      case COORDINATE_DETAILS:
      case MOVIE_DETAILS:
        switch (section) {
          case RELATED_COORD_DESC:
          case RELATED_GOODS_DESC:
            return true;
        }
        break;

      case NEWS:
        switch (section) {
          case NEWS_DESC:
            return true;
        }
        break;

      case REVIEW:
        switch (section) {
          case STYLIST_REVIEW_DESC:
          case RELATED_GOODS_DESC:
            return true;
        }
        break;

      case STYLIST:
        switch (section) {
          case COORD_DESC:
            return true;
        }
        break;

      case TERMS_OF_SERVICE:
        switch (section) {
          case TERMS_OF_SERVICE:
            return true;
        }
        break;

      case PRIVACY_POLICY:
        switch (section) {
          case PRIVACY_POLICY:
            return true;
        }
        break;

      case FAQ:
        switch (section) {
          case FAQ:
            return true;
        }
        break;

      case SPECIFIED_COMMERICAL_TRANSACTION_LAW:
        switch (section) {
          case SPECIFIED_COMMERICAL_TRANSACTION_LAW:
            return true;
        }
        break;

      case COPYRIGHT_INFORMATION:
        switch (section) {
          case COPYRIGHT_INFORMATION:
            return true;
        }
        break;

      case HELP:
        switch (section) {
          case HELP:
            return true;
        }
        break;

      case LANDING_PAGE:
        switch (section) {
          case EYE_CATCH_IMAGE:
          case INTRO_DESC:
          case MOVIE_DESC:
          case COORD_DESC:
          case GOODS_DESC:
          case AFFILIATE_LP_TAGS:
            return true;
        }
        break;

      case CLUSTER_LANDING_PAGE:
        switch (section) {
          case CLUSTER_LP_IMAGE_URL:
          case CLUSTER_LP_DESC:
            return true;
        }
        break;

      case OCCASION_LANDING_PAGE:
        switch (section) {
          case OCCASION_LP_IMAGE_URL:
          case OCCASION_LP_DESC:
            return true;
        }
        break;

      default:
        return false;
    }
    return false;
  }

  /**
   * Validate publish request enums (movie, coordinate, goods)
   *
   * @param publishRequestType
   * @param requestType
   * @return
   */
  public static boolean validateRequestType(int publishRequestType, RequestType requestType) {
    switch (requestType) {
      case DISPLAY_MOVIE:
        switch (PublishRequestType.fromMenuId(publishRequestType)) {
          case MOVIES:
            return true;
        }
        break;
      case DISPLAY_COORDINATE:
        switch (PublishRequestType.fromMenuId(publishRequestType)) {
          case COORD_GOODS_PHOTO:
          case COORD_ONLY_GOODS:
          case COORD_ONLY_PHOTO:
            return true;
        }
        break;
      case DISPLAY_PRODUCTS:
        switch (PublishRequestType.fromMenuId(publishRequestType)) {
          case GOODS_TOPS:
          case GOODS_BOTTOMS:
          case GOODS_ALL_IN_ONE:
          case GOODS_OTHERS:
            return true;
        }
        break;
      default:
        return false;
    }
    return false;
  }

  public static boolean isVideoFileTypeValid(MultipartFile file) {
    return null != file && Objects.requireNonNull(file.getContentType()).equalsIgnoreCase(
        ApplicationConstants.VIDEO_MP4);
  }

  public static boolean isImageTypeValid(MultipartFile file) {
    return null != file && null != SupportedImageType
        .forImageType(Objects.requireNonNull(file.getContentType())
            .split(ApplicationConstants.StringUtils.BACK_SLASH).length > ApplicationConstants.ONE ?
            file.getContentType()
                .split(ApplicationConstants.StringUtils.BACK_SLASH)[ApplicationConstants.ONE] : "");
  }

  public static boolean isFavIconImageTypeValid(MultipartFile file) {
    return SupportedImageType.forImageType(Objects.requireNonNull(file.getContentType())
        .split(ApplicationConstants.StringUtils.BACK_SLASH).length > ApplicationConstants.ONE ?
        file.getContentType()
            .split(ApplicationConstants.StringUtils.BACK_SLASH)[ApplicationConstants.ONE] : "")
        .equals(SupportedImageType.ICO);
  }

  public static <T> boolean genericCheckNotNull(T item) {
    return null != item;
  }

  public static boolean isImageFileSizeValid(MultipartFile file) {
    return file.getSize() <= ApplicationConstants.IMAGE_FILE_SIZE_BYTE;
  }

}
