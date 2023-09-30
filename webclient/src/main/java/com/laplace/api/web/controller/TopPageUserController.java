package com.laplace.api.web.controller;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.TOP_PAGE_USER)
public class TopPageUserController {

  private final WMCUserService wmcUserService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public TopPageUserController(WMCUserService wmcUserService,
      AuthenticationFacade authenticationFacade) {
    this.wmcUserService = wmcUserService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping(APIEndPoints.BEST_SELLER)
  public BaseResponse findBestSeller(Pageable page) {
    return BaseResponse.create(wmcUserService.findBestSeller(page));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse findSellerProfile(@PathVariable("id") Integer id,
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(wmcUserService.findSellerProfile(id, userId));
  }
}
