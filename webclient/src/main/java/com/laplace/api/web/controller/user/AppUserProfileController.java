package com.laplace.api.web.controller.user;

import static com.laplace.api.common.constants.enums.ResponseType.RESULT;

import com.laplace.api.common.dto.request.AccountWithdrawRequestDto;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.Messages;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.auth.JwtAuthenticationToken;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.core.bean.AddressUpdateRequest;
import com.laplace.api.web.core.bean.AppUserBasicInfoRequest;
import com.laplace.api.web.core.bean.AuthenticationRequestDTO;
import com.laplace.api.web.core.bean.EmailUpdateRequest;
import com.laplace.api.web.core.bean.PasswordUpdateRequest;
import com.laplace.api.web.service.AppUserProfileService;
import java.util.Collections;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.APP_USER)
@Validated
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class AppUserProfileController {

  private final AppUserProfileService appUserProfileService;
  private final Messages messages;

  @Autowired
  public AppUserProfileController(AppUserProfileService appUserProfileService,
      Messages messages) {
    this.appUserProfileService = appUserProfileService;
    this.messages = messages;
  }

  @PutMapping(value = APIEndPoints.PROFILE)
  public BaseResponse updateUserBasicInfo(
      @Valid @RequestBody AppUserBasicInfoRequest basicInfoRequest) {
    return BaseResponse.builder()
        .responseType(RESULT)
        .result(appUserProfileService.updateBasicInfo(basicInfoRequest))
        .message(Collections.singletonList(messages.getUpdateBasicInfo()))
        .build();
  }

  @PostMapping(value = APIEndPoints.EMAIL)
  public BaseResponse addEmail(@Valid @RequestBody EmailUpdateRequest request,
      JwtAuthenticationToken token) {
    return BaseResponse.builder()
        .responseType(RESULT)
        .result(appUserProfileService.setUserEmail(request))
        .message(Collections.singletonList(messages.getEmailSent()))
        .build();
  }

  @PutMapping(value = APIEndPoints.PASSWORD)
  public BaseResponse updateUserPassword(@Valid @RequestBody PasswordUpdateRequest request,
      JwtAuthenticationToken token) {
    appUserProfileService.updateUserPassword(request);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singletonList(messages.getUpdatePassword()))
        .build();
  }

  @PutMapping(value = APIEndPoints.PROFILE_ADDRESS)
  public BaseResponse updateUserAddress(@Valid @RequestBody AddressUpdateRequest request,
      JwtAuthenticationToken token) {
    return BaseResponse.builder()
        .result(appUserProfileService.updateUserAddress(request))
        .responseType(RESULT)
        .message(Collections.singletonList(messages.getUpdateAddress()))
        .build();
  }

  @GetMapping(value = APIEndPoints.PROFILE)
  public BaseResponse getUserProfileInformation(JwtAuthenticationToken token) {
    return BaseResponse.builder()
        .result(appUserProfileService.getProfileInformation())
        .responseType(RESULT)
        .build();
  }

  @PutMapping(value = APIEndPoints.TOGGLE_SNS_AUTH)
  public BaseResponse toggleSNSAuth(@RequestBody AuthenticationRequestDTO request) {
    return BaseResponse.builder()
        .responseType(RESULT)
        .result(appUserProfileService.toggleSNSAuth(request))
        .message(Collections.singletonList(messages.getToggleSNS()))
        .build();
  }

  @GetMapping(APIEndPoints.LOGOUT)
  public BaseResponse logout(JwtAuthenticationToken token) {
    UserContext userContext = (UserContext) token.getPrincipal();
    appUserProfileService.logout(userContext.getUserId());
    return BaseResponse.builder()
        .responseType(RESULT)
        .result(Collections.singleton(messages.getLogoutSuccess()))
        .build();
  }

  @DeleteMapping()
  public BaseResponse withdrawAccount(
      @Valid @RequestBody AccountWithdrawRequestDto accountWithdrawRequestDto) {

    appUserProfileService.withdrawAccount(accountWithdrawRequestDto);
    return BaseResponse.create();
  }
}
