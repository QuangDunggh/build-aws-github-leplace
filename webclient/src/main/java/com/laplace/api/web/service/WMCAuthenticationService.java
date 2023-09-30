package com.laplace.api.web.service;

import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.datastax.driver.core.utils.UUIDs;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.VerificationTokenType;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.dto.request.ForgotPasswordRequest;
import com.laplace.api.common.dto.request.ResetPasswordRequest;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.VerificationToken;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.service.TokenService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.model.TwitterAccessTokenDTO;
import com.laplace.api.security.service.JwtTokenService;
import com.laplace.api.security.service.VerificationTokenService;
import com.laplace.api.security.social.service.FacebookService;
import com.laplace.api.security.social.service.TokenStatus;
import com.laplace.api.security.social.service.TwitterService;
import com.laplace.api.web.core.bean.ActivationRequestDTO;
import com.laplace.api.web.core.bean.AuthenticationRequestDTO;
import com.laplace.api.web.core.dto.AuthenticationResponseDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class WMCAuthenticationService {

  private static final Set<Integer> EMAIL_OTP_TOKEN_TYPES = Stream
      .of(VerificationTokenType.EMAIL_OTP, VerificationTokenType.UPDATE_EMAIL_OTP)
      .collect(Collectors.toSet());

  private final AppUserService appUserService;
  private final VerificationTokenService verificationTokenService;
  private final JwtTokenService jwtTokenService;
  private final PasswordEncoder passwordEncoder;
  private final FacebookService facebookService;
  private final TwitterService twitterService;
  private final WMCEmailService emailService;
  private final Messages messages;
  private final TokenService tokenService;
  private final ItemService itemService;

  @Autowired
  public WMCAuthenticationService(AppUserService appUserService,
      VerificationTokenService verificationTokenService,
      JwtTokenService jwtTokenService, PasswordEncoder passwordEncoder,
      FacebookService facebookService, TwitterService twitterService,
      WMCEmailService emailService, Messages messages,
      TokenService tokenService, ItemService itemService) {
    this.appUserService = appUserService;
    this.verificationTokenService = verificationTokenService;
    this.jwtTokenService = jwtTokenService;
    this.passwordEncoder = passwordEncoder;
    this.facebookService = facebookService;
    this.twitterService = twitterService;
    this.emailService = emailService;
    this.messages = messages;
    this.tokenService = tokenService;
    this.itemService = itemService;
  }

  /**
   * Activate user if given token is valid & not expired and give access token
   *
   * @param request acivation request containing token
   * @return authentication response
   */
  @Transactional
  public AuthenticationResponseDTO activateUser(ActivationRequestDTO request) {

    VerificationToken token = verificationTokenService
        .verifyToken(request.getToken(), AppType.WEB_CLIENT, EMAIL_OTP_TOKEN_TYPES)
        .orElseThrow(() -> returnApplicationException(ResultCodeConstants.INVALID_TOKEN));

    if (appUserService.existsByEmailAndActiveTrue(token.getContext())) {
      throwApplicationException(ResultCodeConstants.ALREADY_USED_EMAIL);
    }
    AppUser appUser = appUserService.findById(token.getUserId()).orElseThrow(
        () -> returnApplicationException(ResultCodeConstants.INVALID_TOKEN));

    if (token.getTokenType() == VerificationTokenType.EMAIL_OTP) {
      if (appUser.isActive()) {
        throwApplicationException(ResultCodeConstants.ALREADY_REGISTER);
      } else {
        appUser.setActive(true);
        appUser.setAccessId(UUIDs.timeBased().toString());
      }
    }

    appUser.setLastUpdatedOn(DateUtil.timeNow());
    if (org.apache.commons.lang3.StringUtils.isNotEmpty(token.getContext())) {
      appUser.setEmail(token.getContext());
    }
    appUser.setSearchKeyword(
        AppUserProfileService.buildSearchKeyword(appUser.getEmail(), appUser.getAppUserProfile()));
    appUserService.saveUser(appUser);
    verificationTokenService.deleteToken(request.getToken());
    if (token.getTokenType() == VerificationTokenType.UPDATE_EMAIL_OTP) {
      itemService.updateItemsSellerEmail(appUser.getUserId(), appUser.getEmail());
      logout(token.getUserId());
    }
    return getAuthenticationResponse(appUser, token.getAuthenticationType());
  }

  @Transactional
  public void logout(Integer userId) {
    List<Token> tokenList = tokenService.findByUserIdAndAppType(userId, AppType.WEB_CLIENT);
    if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(tokenList)) {
      tokenService.deleteAll(tokenList);
    }
  }

  /**
   * This method validated basic , facebook & twitter authentication and returns access token &
   * access id
   *
   * @param request authentication request
   * @return authentication response
   */
  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
    AuthenticationResponseDTO responseDTO;
    switch (request.getType()) {
      case BASIC:
        responseDTO = basicAuthentication(request);
        break;
      case FACEBOOK:
        responseDTO = facebookAuthentication(request);
        break;
      case TWITTER:
        responseDTO = twitterTokenVerify(request);
        break;
      default:
        throw new IllegalArgumentException(messages.getInvalidAuthenticationType());
    }
    return responseDTO;
  }

  public void generateResetPasswordTokenAndSendMail(ForgotPasswordRequest request) {
    AppUser user = appUserService.findByEmail(request.getEmail())
        .filter(AppUser::isActive)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_ACTIVE));

    VerificationToken token = verificationTokenService
        .generateResetPasswordToken(user.getUserId(), AppType.WEB_CLIENT,
            ApplicationConstants.VerificationTokenType.RESET_PASSWORD);

    emailService.sendResetPasswordMail(user, token, request.getLang());
  }

  @Transactional
  public void resetPassword(ResetPasswordRequest request) {
    VerificationToken verificationToken = verificationTokenService
        .verifyToken(request.getOtp(), AppType.WEB_CLIENT, VerificationTokenType.RESET_PASSWORD)
        .orElseThrow(() -> returnApplicationException(ResultCodeConstants.INVALID_TOKEN));

    AppUser user = appUserService.findById(verificationToken.getUserId())
        .filter(AppUser::isActive)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_EXISTS));

    savePassword(user, request.getNewPassword());
    verificationTokenService.deleteToken(request.getOtp());
    List<Token> tokenList = tokenService.findByUserId(verificationToken.getUserId()).stream()
        .filter(token ->
            token.getAppType() == AppType.WEB_CLIENT).collect(Collectors.toList());
    tokenService.deleteAll(tokenList);
    emailService.sendPasswordResetSuccessMail(user, Languages.JAPANESE);

  }

  public TwitterAccessTokenDTO getTwitterAccessToken(String oauthToken, String oauthVerifier) {
    TwitterAccessTokenDTO accessToken = twitterService.getAccessToken(oauthToken, oauthVerifier);
    accessToken.setExist(appUserService.findByTwitterId(accessToken.getTwitterId()).isPresent());
    return accessToken;
  }

  AuthenticationResponseDTO getAuthenticationResponse(AppUser user, Integer authenticationType) {
    if (user.isAccountWithdrawn() || user.getUserStatus().equals(UserStatus.BLACK_LISTED)) {
      throw throwApplicationException(ResultCodeConstants.AUTH_FAILURE);
    }

    if (user.getAppUserProfile() != null) {
      return AuthenticationResponseDTO.builder()
          .accessToken(
              jwtTokenService.getTokens(user, authenticationType, AppType.WEB_CLIENT).getAccessToken().getToken())
          .profileImage(user.getAppUserProfile().getProfileImage())
          .email(user.getEmail())
          .firstName(user.getAppUserProfile().getFirstName())
          .familyName(user.getAppUserProfile().getLastName())
          .profileComplete(
              !ObjectUtils.isEmpty(user.getProfileComplete()) ? user.getProfileComplete()
                  : Boolean.FALSE)
          .isActive(user.isActive())
          .build();
    } else {
      return AuthenticationResponseDTO.builder()
          .accessToken(
              jwtTokenService.getTokens(user, authenticationType, AppType.WEB_CLIENT).getAccessToken().getToken())
          .isActive(user.isActive())
          .build();
    }
  }

  private AuthenticationResponseDTO facebookAuthentication(AuthenticationRequestDTO request) {
    TokenStatus tokenStatus = facebookTokenVerification(request.getSnsAccessToken(),
        request.getSocialId());
    AppUser appUser = appUserService.findByFacebookId(tokenStatus.getUserId())
        .filter(AppUser::isFacebookEnable)
        .filter(user -> user.getFacebookUserId().equals(tokenStatus.getUserId()))
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_ACTIVE));

    verifyIfAccountActive(appUser);

    return getAuthenticationResponse(appUser, request.getType().getValue());
  }

  private void verifyIfAccountActive(AppUser appUser) {
    if (!appUser.isActive()) {
      throw throwApplicationException(ResultCodeConstants.ACTIVATION_FAILURE);
    }
  }

  private AuthenticationResponseDTO basicAuthentication(AuthenticationRequestDTO request) {

    if (!StringUtils.hasText(request.getEmail())
        || !request.getEmail().matches(ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX)) {
      throwApplicationException(ResultCodeConstants.INVALID_EMAIL_REGEX);
    } else if (!StringUtils.hasText(request.getPassword())
        || !request.getPassword().matches(ApplicationConstants.VALID_PASSWORD_REGEX)) {
      throwApplicationException(ResultCodeConstants.INVALID_PASSWORD_PATTERN);
    }

    AppUser appUser = appUserService.findByEmail(request.getEmail())
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.AUTH_FAILURE));

    if (!passwordEncoder.matches(request.getPassword(), appUser.getPassword())) {
      throwApplicationException(ResultCodeConstants.AUTH_FAILURE);
    }

    verifyIfAccountActive(appUser);

    return getAuthenticationResponse(appUser, request.getType().getValue());
  }

  private AuthenticationResponseDTO twitterTokenVerify(AuthenticationRequestDTO request) {
    TwitterAccessTokenDTO token = getTwitterAccessToken(request.getSnsAccessToken(),
        request.getTwitterTokenSecret());
    AppUser appUser = appUserService.findByTwitterId(token.getTwitterId())
        .filter(AppUser::isTwitterEnable)
        .filter(user -> user.getTwitterUserId().equals(token.getTwitterId()))
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_ACTIVE));

    verifyIfAccountActive(appUser);

    return getAuthenticationResponse(appUser, request.getType().getValue());
  }

  TokenStatus twitterTokenVerify(String snsAccessToken, String twitterTokenSecret) {
    if (!StringUtils.hasText(snsAccessToken) ||
        !StringUtils.hasText(twitterTokenSecret)) {
      throwApplicationException(ResultCodeConstants.ACCESS_KEY_REQUIRED);
    }
    TokenStatus tokenStatus = twitterService.verify(snsAccessToken, twitterTokenSecret);
    if (!tokenStatus.isValid()) {
      throwApplicationException(ResultCodeConstants.INVALID_ACCESS_KEY);
    }
    return tokenStatus;
  }

  TokenStatus facebookTokenVerification(String snsAccessToken, String socialId) {
    if (!StringUtils.hasText(snsAccessToken)) {
      throwApplicationException(ResultCodeConstants.ACCESS_KEY_REQUIRED);
    }
    TokenStatus tokenStatus = facebookService.verify(snsAccessToken, socialId);
    if (!tokenStatus.isValid()) {
      throwApplicationException(ResultCodeConstants.INVALID_ACCESS_KEY);
    }
    return tokenStatus;
  }

  private void savePassword(AppUser user, String password) {
    user.setPassword(passwordEncoder.encode(password));
    user.setLastUpdatedOn(DateUtil.timeNow());
    appUserService.saveUser(user);
  }

}
