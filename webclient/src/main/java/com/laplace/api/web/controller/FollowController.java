package com.laplace.api.web.controller;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.request.FollowDTO;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCFollowService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.FOLLOW)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class FollowController {

  private final WMCFollowService wmcFollowService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  FollowController(WMCFollowService wmcFollowService,
      AuthenticationFacade authenticationFacade) {
    this.wmcFollowService = wmcFollowService;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping
  public BaseResponse isUserFollowed(@PathVariable("id") Integer id,
      @RequestHeader(value = ApplicationConstants.AUTH_HEADER_NAME, required = false) String token) {
    Integer userId = authenticationFacade.getUserId(token);
    return BaseResponse.create(wmcFollowService.isUserFollowed(id, userId));
  }

  @PutMapping
  public BaseResponse followUser(@PathVariable("id") Integer id,
      @Valid @RequestBody FollowDTO followDTO) {
    wmcFollowService.followUser(id, followDTO);
    return BaseResponse.create();
  }
}
