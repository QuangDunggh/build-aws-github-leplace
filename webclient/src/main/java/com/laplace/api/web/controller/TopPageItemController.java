package com.laplace.api.web.controller;


import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.FilteringCriteria;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.response.ItemBasicResponseDto;
import com.laplace.api.common.util.AcceptLanguageUtil;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.constants.RequestParams;
import com.laplace.api.web.core.dto.SellerItemStatus;
import com.laplace.api.web.service.WMCItemService;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.TOP_PAGE_ITEMS)
public class TopPageItemController {

  private final WMCItemService wmcItemService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public TopPageItemController(WMCItemService wmcItemService,
      AuthenticationFacade authenticationFacade) {
    this.wmcItemService = wmcItemService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping()
  public BaseResponse findItemsByCriteria(
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      @RequestParam(RequestParams.FILTERING_CRITERIA) FilteringCriteria filteringCriteria,
      Pageable pageable) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse
        .create(wmcItemService.findItemsByCriteria(filteringCriteria, userId, pageable));
  }

  @GetMapping(APIEndPoints.NEW_ARRIVAL)
  public BaseResponse findNewArrivalItems(
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      Pageable page) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(wmcItemService.findNewArrivalItems(userId, page));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse findItem(@PathVariable(ApplicationConstants.ID) String id,
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(wmcItemService.getItemDetails(userId, id));
  }

  @PostMapping(APIEndPoints.SEARCH)
  public BaseResponse searchItems(
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      @Valid @RequestBody ItemSearchRequest itemSearchRequest) {
    Integer userId = authenticationFacade.getUserId(token);
    if (AcceptLanguageUtil.getLanguage().equals(Languages.ENGLISH)) {
      itemSearchRequest.setLang(Languages.ENGLISH);
    }
    return BaseResponse.create(wmcItemService.itemSearch(userId, itemSearchRequest));
  }

  @GetMapping(APIEndPoints.POPULAR_DESIGNER)
  public BaseResponse popularItems(
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      @RequestParam(value = "sort", required = false, defaultValue = "1") Integer sort,
      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(wmcItemService.popularDesignerItems(userId, page, size, sort));
  }

  @GetMapping(APIEndPoints.USER_CHOICE)
  public BaseResponse findUserChoiceItems(
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      Pageable page) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(wmcItemService.findUserChoiceItems(userId, page));
  }

  @GetMapping(APIEndPoints.RELATED_ITEMS)
  public BaseResponse getRelatedItems(
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      @RequestParam(value = RequestParams.ITEM_ID) String itemId,
      @RequestParam(value = RequestParams.BRAND_ID) Integer brandId,
      @RequestParam(value = RequestParams.CATEGORY) Category category, Pageable pageable) {
    Integer userId = authenticationFacade.getUserId(token);
    PageableResponseDTO<ItemBasicResponseDto> relatedItems = wmcItemService
        .getRelatedItems(userId, itemId, brandId, category, pageable);
    return BaseResponse.create(relatedItems);
  }

  @GetMapping(APIEndPoints.SELLER_ALL_ITEMS)
  public BaseResponse getSellerAllItems(@PathVariable("id") Integer id,
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token,
      @RequestParam(value = RequestParams.FILTER, required = false)
          Set<SellerItemStatus> sellerItemStatus, Pageable pageable) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(
        wmcItemService.getSellerAllItems(id, userId, sellerItemStatus, pageable));
  }
}
