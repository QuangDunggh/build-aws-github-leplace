package com.laplace.api.cms.controller;

import static com.laplace.api.common.constants.enums.ResponseType.RESULT;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.core.bean.ChangePasswordRequest;
import com.laplace.api.cms.core.bean.LoginRequestDto;
import com.laplace.api.cms.core.bean.SignUpRequest;
import com.laplace.api.cms.core.dto.LogInResponseDto;
import com.laplace.api.cms.service.CMSAuthService;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.dto.request.ForgotPasswordRequest;
import com.laplace.api.common.dto.request.ResetPasswordRequest;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.config.SecurityConfigConstants;
import com.laplace.api.security.helper.AuthenticationFacade;
import java.util.Collections;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.AUTH)
public class CMSAuthController {

  private final CMSAuthService cmsAuthService;
  private final Messages messages;
  private final AuthenticationFacade authenticationFacade;


  @Autowired
  public CMSAuthController(CMSAuthService cmsAuthService,
      Messages messages,
      AuthenticationFacade authenticationFacade) {
    this.cmsAuthService = cmsAuthService;
    this.messages = messages;
    this.authenticationFacade = authenticationFacade;
  }

  @PostMapping
  public BaseResponse login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
    LogInResponseDto responseDto = cmsAuthService.login(loginRequestDto);
    return BaseResponse.builder()
        .responseType(RESULT)
        .result(responseDto)
        .build();
  }

  @PostMapping(APIEndPoints.FORGOT_PASSWORD)
  public BaseResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    request.setLang(Languages.JAPANESE);
    cmsAuthService.generateResetPasswordTokenAndSendMail(request);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(messages.getForgotPasswordMailSent()))
        .build();
  }

  @PutMapping(APIEndPoints.RESET_PASSWORD)
  public BaseResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    cmsAuthService.resetPassword(request);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(messages.getPasswordUpdate()))
        .build();
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
