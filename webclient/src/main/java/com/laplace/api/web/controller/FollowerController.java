package com.laplace.api.web.controller;

import com.laplace.api.common.constants.enums.FollowerCriteria;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.constants.RequestParams;
import com.laplace.api.web.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.FOLLOWER)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class FollowerController {

  private final AuthenticationFacade authenticationFacade;
  private final FollowerService followerService;

  @Autowired
  public FollowerController(
      AuthenticationFacade authenticationFacade,
      FollowerService followerService) {
    this.authenticationFacade = authenticationFacade;
    this.followerService = followerService;
  }

  @GetMapping
  public BaseResponse getFollowers(
      @RequestParam(RequestParams.CRITERIA) FollowerCriteria criteria) {
    Integer userId = authenticationFacade.getUserId();
    return BaseResponse.create(followerService.getFollowers(userId, criteria));
  }
}
