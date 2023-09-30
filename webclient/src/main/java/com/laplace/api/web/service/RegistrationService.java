package com.laplace.api.web.service;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.VerificationTokenType;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.AuthenticationType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.Messages;
import com.laplace.api.common.validators.PasswordConstraintValidator;
import com.laplace.api.security.model.TwitterAccessTokenDTO;
import com.laplace.api.security.service.VerificationTokenService;
import com.laplace.api.security.social.service.TokenStatus;
import com.laplace.api.web.core.bean.SignUpRequestDTO;
import com.laplace.api.web.core.dto.AuthenticationResponseDTO;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RegistrationService {

  private final AppUserService userService;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenService verificationTokenService;
  private final WMCEmailService emailService;
  private final WMCAuthenticationService authenticationService;
  private final Messages messages;

  @Autowired
  public RegistrationService(AppUserService userService, PasswordEncoder passwordEncoder,
      VerificationTokenService verificationTokenService, WMCEmailService emailService,
      WMCAuthenticationService authenticationService, Messages messages) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.verificationTokenService = verificationTokenService;
    this.emailService = emailService;
    this.authenticationService = authenticationService;
    this.messages = messages;
  }

  /**
   * This method register user through basic credential/facebook/twitter. If user registers with
   * social access token is given in response,else send a otp to email for activation
   *
   * @param signUpRequest signup request
   * @return Optional of authentication response. Value is present, when type is facebook/twitter.
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   */
  @Transactional
  public Optional<AuthenticationResponseDTO> register(SignUpRequestDTO signUpRequest)
      throws InvalidKeyException,
      NoSuchAlgorithmException {

    switch (signUpRequest.getType()) {
      case BASIC:
        registerWithBasicCredential(signUpRequest);
        return Optional.empty();
      case FACEBOOK:
        return registerWithFacebook(signUpRequest);
      case TWITTER:
        return registerWithTwitter(signUpRequest);
      default:

    }
    throw new IllegalArgumentException(messages.getInvalidAuthenticationType());
  }

  private Optional<AuthenticationResponseDTO> registerWithTwitter(SignUpRequestDTO request) {
    TwitterAccessTokenDTO accessToken = authenticationService
        .getTwitterAccessToken(request.getSnsAccessToken(),
            request.getTwitterTokenSecret());
    request.setSocialId(accessToken.getTwitterId());
    AppUser user = userService.findByTwitterId(request.getSocialId())
        .orElseGet(() -> convert(request));

    return completeSocialRegistration(user, request.getType());
  }

  private Optional<AuthenticationResponseDTO> registerWithFacebook(SignUpRequestDTO request) {
    TokenStatus tokenStatus = authenticationService
        .facebookTokenVerification(request.getSnsAccessToken(), request.getSocialId());
    request.setSocialId(tokenStatus.getUserId());
    AppUser user = userService.findByFacebookId(request.getSocialId())
        .orElseGet(() -> convert(request));

    return completeSocialRegistration(user, request.getType());
  }

  private Optional<AuthenticationResponseDTO> completeSocialRegistration(AppUser user, AuthenticationType authenticationType) {
    if (user.isActive()) {
      throwApplicationException(ResultCodeConstants.ALREADY_REGISTER);
    }
    if (StringUtils.hasText(user.getEmail())) {
      userService.findByEmail(user.getEmail())
          .ifPresent(u -> throwApplicationException(ResultCodeConstants.ALREADY_USED_EMAIL));
      user.setActive(true);
    }
    user.setAccessId(DateUtil.getUniqueTimeBasedUUID());
    userService.saveUser(user);
    return Optional.of(authenticationService.getAuthenticationResponse(user, authenticationType.getValue()));
  }

  private void registerWithBasicCredential(SignUpRequestDTO signUpRequest) {
    if (!StringUtils.hasText(signUpRequest.getPassword()) || !signUpRequest.getPassword()
        .matches(ApplicationConstants.VALID_PASSWORD_REGEX) || !PasswordConstraintValidator
        .isValidPasswordLength(signUpRequest.getPassword())) {
      throwApplicationException(ResultCodeConstants.INVALID_PASSWORD_PATTERN);
    }

    if (!StringUtils.hasText(signUpRequest.getEmail()) || !signUpRequest.getEmail()
        .matches(ApplicationConstants.VALID_EMAIL_ADDRESS_REGEX)) {
      throwApplicationException(ResultCodeConstants.INVALID_EMAIL_REGEX);
    }

    AppUser user = userService.findByEmail(signUpRequest.getEmail())
        .orElseGet(() -> convert(signUpRequest));

    if (user.isActive()) {
      throwApplicationException(ResultCodeConstants.ALREADY_REGISTER);
    }

    populateCommonProperties(user);

    if (ObjectUtils.isEmpty(user.getUserId())) {
      userService.saveUser(user);
    }

    sendEmailActivationMail(user.getEmail(), user.getUserId(), signUpRequest.getLang(),
        ApplicationConstants.VerificationTokenType.EMAIL_OTP);
  }

  public void sendEmailActivationMail(String mail, Integer userId, String lang, Integer tokenType) {
    emailService.sendActivationEmail(mail, verificationTokenService
        .generateOTP(userId, AppType.WEB_CLIENT, tokenType, mail), lang);
  }

  private void populateCommonProperties(AppUser user) {
    user.setCreatedOn(DateUtil.timeNow());
    user.setLastUpdatedOn(user.getCreatedOn());
    user.setUserStatus(UserStatus.NORMAL);
    user.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
    user.setSearchKeyword(user.getEmail());
    user.setAccountWithdrawn(false);
    user.setUnreadCount(ApplicationConstants.ZERO);
  }

  private AppUser convert(SignUpRequestDTO requestDTO) {
    AppUser user = new AppUser();
    user.setEmail(requestDTO.getEmail());
    switch (requestDTO.getType()) {
      case FACEBOOK:
        user.setFacebookUserId(requestDTO.getSocialId());
        requestDTO.setPassword(UUID.randomUUID().toString());
        user.setFacebookEnable(true);
        break;
      case TWITTER:
        user.setTwitterUserId(requestDTO.getSocialId());
        requestDTO.setPassword(UUID.randomUUID().toString());
        user.setTwitterEnable(true);
        break;
    }
    user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
    populateCommonProperties(user);
    return user;
  }
}
