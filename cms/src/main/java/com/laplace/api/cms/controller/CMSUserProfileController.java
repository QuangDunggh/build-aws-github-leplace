package com.laplace.api.cms.controller;

import static com.laplace.api.common.constants.enums.ResponseType.RESULT;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.core.bean.ChangePasswordRequest;
import com.laplace.api.cms.service.CMSAuthService;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.config.SecurityConfigConstants;
import com.laplace.api.security.helper.AuthenticationFacade;
import java.util.Collections;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.USER)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CMSUserProfileController {

  private final CMSAuthService cmsAuthService;
  private final Messages messages;
  private AuthenticationFacade authenticationFacade;


  @Autowired
  public CMSUserProfileController(CMSAuthService cmsAuthService,
      Messages messages,
      AuthenticationFacade authenticationFacade) {
    this.cmsAuthService = cmsAuthService;
    this.messages = messages;
    this.authenticationFacade = authenticationFacade;
  }

  @PutMapping(APIEndPoints.CHANGE_PASSWORD)
  public BaseResponse changePassword(
      @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
    Integer userId = authenticationFacade.getUserContext().get().getUserId();
    cmsAuthService.changePassword(changePasswordRequest, userId);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(messages.getPasswordUpdate()))
        .build();
  }

  @GetMapping(APIEndPoints.LOGOUT)
  public BaseResponse logout(
      @RequestHeader(value = SecurityConfigConstants.AUTHENTICATION_HEADER_NAME) String token) {
    cmsAuthService.logout(token);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(messages.getLogoutSuccess()))
        .build();
  }
}
