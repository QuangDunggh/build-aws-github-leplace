package com.laplace.api.cms.controller;

import static com.laplace.api.common.constants.enums.ResponseType.RESULT;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.constants.CmsApplicationConstants.RequestParams;
import com.laplace.api.cms.service.UserService;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.util.BaseResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(APIEndPoints.CMS_USER)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CMSUserController {

  private final UserService userService;

  @Autowired
  public CMSUserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse getAppUserList(
      @RequestParam(value = RequestParams.KEYWORD, required = false) String keyword,
      @RequestParam(value = RequestParams.VERIFICATION_STATUS, required = false) List<VerificationStatus> verificationStatus,
      Pageable pageable) {
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(HttpStatus.OK.getReasonPhrase()))
        .result(userService.getAppUserList(keyword, verificationStatus, pageable))
        .build();
  }

  @GetMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse getAppUserInfo(@PathVariable("userId") Integer userId) {
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(HttpStatus.OK.getReasonPhrase()))
        .result(userService.getAppUserProfile(userId))
        .build();
  }

  @PutMapping("/{userId}/status/update")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse changeUserStatus(@PathVariable("userId") Integer userId) {
    userService.changeUserStatus(userId);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(HttpStatus.OK.getReasonPhrase()))
        .build();
  }

  @PatchMapping("/{userId}/verify")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse changeVerificationStatus(@PathVariable("userId") Integer userId,
      @RequestParam(value = RequestParams.VERIFICATION_STATUS) VerificationStatus verificationStatus) {
    userService.changeVerificationStatus(userId, verificationStatus);
    return BaseResponse.create();
  }
}
