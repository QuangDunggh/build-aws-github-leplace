package com.laplace.api.web.controller;

import static com.laplace.api.common.constants.enums.ResponseType.RESULT;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.request.ForgotPasswordRequest;
import com.laplace.api.common.dto.request.ResetPasswordRequest;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.LaplaceResponseUtil;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.social.service.TwitterService;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.core.bean.ActivationRequestDTO;
import com.laplace.api.web.core.bean.AuthenticationRequestDTO;
import com.laplace.api.web.core.bean.SignUpRequestDTO;
import com.laplace.api.web.core.dto.AuthenticationResponseDTO;
import com.laplace.api.web.service.RegistrationService;
import com.laplace.api.web.service.WMCAuthenticationService;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

@RestController
@RequestMapping(APIEndPoints.AUTH)
@Validated
@Slf4j
public class WMCAuthController {

  private final RegistrationService registrationService;
  private final WMCAuthenticationService authenticationService;
  private final Messages messages;
  private final TwitterService twitterService;

  @Autowired
  public WMCAuthController(RegistrationService registrationService,
      WMCAuthenticationService authenticationService,
      Messages messages, TwitterService twitterService) {
    this.registrationService = registrationService;
    this.authenticationService = authenticationService;
    this.messages = messages;
    this.twitterService = twitterService;
  }

  @PostMapping
  public BaseResponse authenticate(@Valid @RequestBody AuthenticationRequestDTO request) {

    return BaseResponse.builder()
        .responseType(ResponseType.RESULT)
        .message(Collections.singleton(HttpStatus.OK.getReasonPhrase()))
        .result(authenticationService.authenticate(request))
        .code(ApplicationConstants.SUCCESS_CODE)
        .build();
  }

  @PostMapping(value = APIEndPoints.SIGN_UP)
  public BaseResponse signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO)
      throws NoSuchAlgorithmException,
      InvalidKeyException {
    signUpRequestDTO.setLang(Languages.JAPANESE);
    Optional<AuthenticationResponseDTO> register = registrationService.register(signUpRequestDTO);
    return BaseResponse.builder()
        .responseType(ResponseType.RESULT)
        .code(ApplicationConstants.SUCCESS_CODE)
        .message(register.isPresent() ? Collections.singleton(messages.getRegistrationSuccess())
            : Collections.singleton(messages.getEmailSent()))
        .result(register.orElse(null))
        .build();
  }

  @PostMapping(value = APIEndPoints.ACTIVATE)
  public BaseResponse active(@Valid @RequestBody ActivationRequestDTO activationRequest) {

    return BaseResponse.builder()
        .responseType(ResponseType.RESULT)
        .message(Collections.singleton(HttpStatus.OK.getReasonPhrase()))
        .result(authenticationService.activateUser(activationRequest))
        .code(ApplicationConstants.SUCCESS_CODE)
        .build();
  }

  @PostMapping(APIEndPoints.FORGOT_PASSWORD)
  public BaseResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    request.setLang(Languages.JAPANESE);
    authenticationService.generateResetPasswordTokenAndSendMail(request);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(messages.getForgotPasswordMailSent()))
        .build();
  }

  @PutMapping(APIEndPoints.RESET_PASSWORD)
  public BaseResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    authenticationService.resetPassword(request);
    return BaseResponse.builder()
        .responseType(RESULT)
        .message(Collections.singleton(messages.getPasswordUpdate()))
        .build();
  }

  @GetMapping(APIEndPoints.TWITTER_REQUEST_TOKEN)
  public BaseResponse getTwitterRequestToken(
      @RequestHeader(value = "Authorization", required = false) String payload,
      @RequestParam(required = false, name = "st") String signUpToken) throws TwitterException {
    RequestToken requestToken = twitterService.getRequestToken(payload, signUpToken);
    return BaseResponse.builder()
        .responseType(RESULT)
        .result(requestToken)
        .code(ErrorCode.AUTHENTICATION)
        .build();
  }

  @GetMapping(APIEndPoints.TWITTER_ACCESS_TOKEN)
  public BaseResponse twitterCallback(
      @RequestParam(value = "oauth_token", required = false) String oauthToken,
      @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier,
      @RequestParam(value = "denied", required = false) String denied) {

    if (denied != null) {
      LaplaceResponseUtil.throwApplicationException(ResultCodeConstants.UNAUTHORIZED_OPERATION);
    }

    return BaseResponse.builder()
        .responseType(RESULT)
        .result(authenticationService.getTwitterAccessToken(oauthToken, oauthVerifier))
        .code(ErrorCode.AUTHENTICATION)
        .build();
  }
}
