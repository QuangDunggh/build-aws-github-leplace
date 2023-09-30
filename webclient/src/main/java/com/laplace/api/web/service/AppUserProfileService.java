package com.laplace.api.web.service;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.StatusConstants;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.AuthenticationType;
import com.laplace.api.common.converter.AccountWithdrawConverter;
import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.dto.request.AccountWithdrawRequestDto;
import com.laplace.api.common.dto.response.AddressUpdateResponse;
import com.laplace.api.common.dto.response.AppUserBasicInfoResponse;
import com.laplace.api.common.dto.response.AppUserProfileResponseDto;
import com.laplace.api.common.dto.response.SNSToggleResponse;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.model.db.VerificationToken;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.repository.db.AppUserProfileRepository;
import com.laplace.api.common.service.*;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.security.model.TwitterAccessTokenDTO;
import com.laplace.api.security.service.VerificationTokenService;
import com.laplace.api.security.social.service.FacebookService;
import com.laplace.api.security.social.service.TokenStatus;
import com.laplace.api.security.social.service.TwitterService;
import com.laplace.api.web.core.bean.*;
import com.laplace.api.web.core.dto.AuthenticationResponseDTO;
import com.laplace.api.web.core.dto.EmailUpdateResponse;
import com.laplace.api.web.core.dto.ToggleSNSAuthResponse;
import java.nio.ByteBuffer;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.laplace.api.common.constants.ApplicationConstants.JP_COUNTRY_CODE;
import static com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import static com.laplace.api.common.constants.ApplicationConstants.StringUtils.PLUS;
import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.*;
import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
public class AppUserProfileService {

  private final AppUserProfileRepository appUserProfileRepository;
  private final AuthenticationFacade authenticationFacade;
  private final WMCAuthenticationService authenticationService;
  private final AppUserService appUserService;
  private final PasswordEncoder passwordEncoder;
  private final WMCAddressService addressService;
  private final FacebookService facebookService;
  private final TwitterService twitterService;
  private final TokenService tokenService;
  private final UserInfoService userInfoService;
  private final ItemService itemService;
  private final AccountWithdrawConverter accountWithdrawConverter;
  private final OrderService orderService;
  private final SellerProfileService sellerProfileService;
  private final WMCEmailService emailService;
  private final VerificationTokenService verificationTokenService;

  @Autowired
  public AppUserProfileService(AppUserProfileRepository appUserProfileRepository,
      AuthenticationFacade authenticationFacade,
      WMCAuthenticationService authenticationService,
      SellerProfileService sellerProfileService,
      AppUserService appUserService,
      PasswordEncoder passwordEncoder,
      WMCAddressService addressService,
      FacebookService facebookService,
      WMCEmailService emailService,
      TwitterService twitterService,
      TokenService tokenService, UserInfoService userInfoService,
      ItemService itemService,
      AccountWithdrawConverter accountWithdrawConverter,
      @Lazy OrderService orderService,
      VerificationTokenService verificationTokenService) {
    this.appUserProfileRepository = appUserProfileRepository;
    this.authenticationFacade = authenticationFacade;
    this.authenticationService = authenticationService;
    this.appUserService = appUserService;
    this.passwordEncoder = passwordEncoder;
    this.addressService = addressService;
    this.facebookService = facebookService;
    this.twitterService = twitterService;
    this.tokenService = tokenService;
    this.userInfoService = userInfoService;
    this.itemService = itemService;
    this.accountWithdrawConverter = accountWithdrawConverter;
    this.orderService = orderService;
    this.sellerProfileService = sellerProfileService;
    this.emailService = emailService;
    this.verificationTokenService = verificationTokenService;
  }

  /**
   * Update app user basic info
   *
   * @param request
   *
   */
  @Transactional
  public AuthenticationResponseDTO updateBasicInfo(AppUserBasicInfoRequest request) {
    UserContext userContext = authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    Integer userId = userContext.getUserId();

    AppUser appUser = appUserService.findById(userId)
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    request.setPhoneNumber(JP_COUNTRY_CODE + request.getPhoneNumber());
    AppUserProfile appUserProfile = appUser.getOrEmptyProfile();
    appUserProfile.setUserId(userId);
    appUserProfile.setFirstName(request.getFirstName());
    appUserProfile.setLastName(request.getLastName());
    appUserProfile.setFirstNameKana(request.getFirstNameKana());
    appUserProfile.setLastNameKana(request.getLastNameKana());
    appUserProfile.setUserName(request.getUserName());
    appUserProfile.setBirthDate(ZonedDateTime.of(request.getBirthYear(), request.getBirthMonth(),
        request.getBirthDay(), VALUE_ZERO, VALUE_ZERO, VALUE_ZERO, VALUE_ZERO, ZoneOffset.UTC));
    appUserProfile.setPhoneNumber(request.getPhoneNumber());
    appUserProfile.setLastUpdatedOn(DateUtil.timeNow());
    appUserProfile.setLastUpdatedBy(userId);
    appUserProfile.setCreatedBy(userId);
    appUserProfile.setCreatedOn(DateUtil.timeNow());
    AuthenticationResponseDTO authResponse = null;
    if (appUser.getProfileComplete() == null || !appUser.getProfileComplete()) {
      appUser.setProfileComplete(true);
      authenticationService.logout(appUser.getUserId());
      authResponse = authenticationService.getAuthenticationResponse(appUser, userContext.getAuthenticationType());
    }
    appUser.setAppUserProfile(appUserProfile);
    appUser.setSearchKeyword(buildSearchKeyword(appUser.getEmail(), appUserProfile));
    appUser.setLastUpdatedOn(DateUtil.timeNow());
    appUserService.saveUser(appUser);
    return authResponse;
  }

  public EmailUpdateResponse setUserEmail(EmailUpdateRequest request) {
    if (org.apache.commons.lang3.StringUtils.isEmpty(request.getNewEmail())) {
      throw throwApplicationException(INVALID_EMAIL);
    }
    Integer userId = authenticationFacade.getUserContext()
        .orElseThrow(() -> returnApplicationException(USER_NOT_EXISTS))
        .getUserId();
    AppUser appUser = appUserService
        .findById(userId)
        .orElseThrow(() -> returnApplicationException(USER_NOT_EXISTS));
    appUserService.findByEmail(request.getNewEmail()).ifPresent(existingUser -> {
      if (existingUser.isActive()) {
        throwApplicationException(ALREADY_USED_EMAIL);
      }
    });

    int tokenType;
    if (appUser.isActive()) {
      tokenType = ApplicationConstants.VerificationTokenType.UPDATE_EMAIL_OTP;
    } else {
      tokenType = ApplicationConstants.VerificationTokenType.EMAIL_OTP;
    }

    if (!appUser.getUserId().equals(userId)) {
      throwApplicationException(USER_DATA_MISMATCHED);
    }
    if (org.apache.commons.lang3.StringUtils.equals(request.getNewEmail(), appUser.getEmail())) {
      throwApplicationException(NOTHING_TO_UPDATE);
    }

    VerificationToken token = verificationTokenService
        .generateOTP(userId, AppType.WEB_CLIENT, tokenType, request.getNewEmail());

    emailService.sendResetEmailAddressMail(request.getNewEmail(), appUser, token,
        Languages.JAPANESE);
    return EmailUpdateResponse.builder().email(request.getNewEmail()).build();
  }

  /**
   * Update user password
   *
   * @param request
   */
  public void updateUserPassword(PasswordUpdateRequest request) {
    Integer userId = authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS))
        .getUserId();
    AppUser appUser = appUserService
        .findById(userId).orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    if (!passwordEncoder.matches(request.getCurrentPass(), appUser.getPassword())) {
      throwApplicationException(OLD_PASSWORD_NOT_MATCHED);
    }
    appUser.setPassword(passwordEncoder.encode(request.getNewPass()));
    appUserService.saveUser(appUser);

    List<Token> tokenList = tokenService.findByUserId(userId).stream().filter(token ->
        token.getAppType() == AppType.WEB_CLIENT).collect(Collectors.toList());
    tokenService.deleteAll(tokenList);
  }

  /**
   * Update user profile address (default)
   *
   * @param request
   * @return
   */
  @Transactional
  public AddressUpdateResponse updateUserAddress(AddressUpdateRequest request) {
    Integer userId = authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS))
        .getUserId();
    appUserService.findById(userId).orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));

    Address address = addressService.getAppUserDefaultAddress(userId)
        .orElseGet(Address::new);

    convertAddressUpdateRequest(request, address, userId);
    addressService.saveUserDefaultAddress(address);

    AppUserProfile appUserProfile = appUserProfileRepository.findById(userId)
        .orElseGet(AppUserProfile::new);
    appUserProfile.setUserId(userId);
    appUserProfile.setAddressId(address.getId());
    appUserProfileRepository.save(appUserProfile);

    return AddressUpdateResponse.builder()
        .id(address.getId())
        .zipCode(address.getZip())
        .updatedAt(DateUtil.toEpochMilli(address.getLastUpdatedOn()))
        .build();
  }

  private void convertAddressUpdateRequest(AddressUpdateRequest request, Address address,
      Integer userId) {
    address.setUserId(userId);
    address.setZip(request.getZipCode());
    address.setAsDefault(true);
  }

  public static String buildSearchKeyword(String email, AppUserProfile appUserProfile) {
    String searchKeyword = StringUtils.EMPTY_STRING;
    searchKeyword += email;
    if (ObjectUtils.isEmpty(appUserProfile)) {
      return searchKeyword;
    }
    searchKeyword += addIfNotEmpty(appUserProfile.getFirstName());
    searchKeyword += addIfNotEmpty(appUserProfile.getLastName());
    searchKeyword += addIfNotEmpty(appUserProfile.getFirstNameKana());
    searchKeyword += addIfNotEmpty(appUserProfile.getLastNameKana());
    return searchKeyword;
  }

  private static String addIfNotEmpty(String str) {
    return ObjectUtils.isEmpty(str) ? StringUtils.EMPTY_STRING : StringUtils.BAR + str;
  }

  private AppUserBasicInfoResponse convertAppUserProfile(AppUserProfile profile) {
    return AppUserBasicInfoResponse.builder()
        .profileImage(profile.getProfileImage())
        .name(profile.getName())
        .firstName(profile.getFirstName())
        .familyName(profile.getLastName())
        .birthDate(null != profile.getBirthDate() ? DateUtil.toEpochMilli(profile.getBirthDate()) : null)
        .phoneNumber(profile.getPhoneNumber())
        .build();
  }

  /**
   * Get user profile information (basic info, email, address)
   *
   * @return
   */
  public AppUserProfileResponseDto getProfileInformation() {
    Integer userId = authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS))
        .getUserId();
    return userInfoService.getUserProfileDetails(userId);
  }

  /**
   * Get facebook and twitter sns info of app user
   *
   * @param appUser
   * @return
   */
  private List<SNSToggleResponse> getSNSInfo(AppUser appUser) {
    List<SNSToggleResponse> responses = new ArrayList<>(2);
    responses.add(SNSToggleResponse.builder()
        .snsType(AuthenticationType.FACEBOOK.getValue())
        .toggleVal(appUser.isFacebookEnable())
        .build());
    responses.add(SNSToggleResponse.builder()
        .snsType(AuthenticationType.TWITTER.getValue())
        .toggleVal(appUser.isTwitterEnable())
        .build());
    return responses;
  }

  /**
   * Toggle sns auth(facebook/twitter) value
   *
   * @param request
   * @return
   */
  public ToggleSNSAuthResponse toggleSNSAuth(AuthenticationRequestDTO request) {
    AppUser appUser = appUserService.findById(authenticationFacade.getUserContext()
        .orElseThrow(() -> throwApplicationException(AUTH_FAILURE)).getUserId())
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    TokenStatus tokenStatus = new TokenStatus();
    ToggleSNSAuthResponse response = ToggleSNSAuthResponse.builder()
        .snsType(request.getType().getValue())
        .build();

    switch (request.getType()) {
      case FACEBOOK:
        if (!appUser.isFacebookEnable()) {
          if (ObjectUtils.isEmpty(request.getSnsAccessToken())) {
            throw throwApplicationException(INVALID_TOKEN);
          }
          tokenStatus = facebookService.verify(request.getSnsAccessToken(), request.getSocialId());
          if (!tokenStatus.isValid()) {
            throw throwApplicationException(INVALID_TOKEN);
          }
          if (appUserService.existsByFacebookUserId(tokenStatus.getUserId())) {
            throw throwApplicationException(DUPLICATE_SNS_PROFILE);
          }
          appUser.setFacebookUserId(tokenStatus.getUserId());
        } else {
          appUser.setFacebookUserId(null);
        }

        appUser.setFacebookEnable(!appUser.isFacebookEnable());
        response.setToggleVal(appUser.isFacebookEnable());
        break;

      case TWITTER:
        if (!appUser.isTwitterEnable()) {
          if (ObjectUtils.isEmpty(request.getSnsAccessToken())) {
            throw throwApplicationException(INVALID_TOKEN);
          }
          TwitterAccessTokenDTO accessToken = twitterService
              .getAccessToken(request.getSnsAccessToken(), request.getTwitterTokenSecret());
          tokenStatus.setValid(null != accessToken);
          appUser.setTwitterUserId(Optional.ofNullable(accessToken)
              .orElseThrow(() -> throwApplicationException(TWITTER_EXCEPTION)).getTwitterId());

          if (!ObjectUtils.isEmpty(request.getSnsAccessToken()) && !tokenStatus.isValid()) {
            throw throwApplicationException(INVALID_TOKEN);
          }
          if (appUserService.findByTwitterId(appUser.getTwitterUserId()).isPresent()) {
            throw throwApplicationException(DUPLICATE_SNS_PROFILE);
          }
        } else {
          appUser.setTwitterUserId(null);
        }
        appUser.setTwitterEnable(!appUser.isTwitterEnable());
        response.setToggleVal(appUser.isTwitterEnable());
        break;
      default:
        throw throwApplicationException(WRONG_SNS_TYPE);
    }
    appUserService.saveUser(appUser);
    sellerProfileService.updateSNS(appUser);
    return response;
  }

  public void logout(Integer userId) {
    List<Token> tokenList = tokenService.findByUserId(userId).stream().filter(token ->
        token.getAppType() == AppType.WEB_CLIENT).collect(Collectors.toList());
    tokenService.deleteAll(tokenList);
  }

  public void withdrawAccount(AccountWithdrawRequestDto requestDto) {
    requestDto.setUserId(authenticationFacade.getUserId());
    AppUser appUser = appUserService.findById(requestDto.getUserId()).orElseThrow(
        () -> throwApplicationException(USER_NOT_EXISTS)
    );
    String email = appUser.getEmail();
    if (findOnSaleOrSoldItemsByUser(requestDto.getUserId()).getTotalElements() > 0) {
      throw throwApplicationException(ITEM_ON_SALE_VALIDATION);
    }

    if (incompleteOrdersByUserExists(requestDto.getUserId())) {
      throw throwApplicationException(ITEM_ON_WAY_VALIDATION);
    }
    appUser.setAccountWithdrawn(true);
    appUser.setAccountWithdrawReason(accountWithdrawConverter.convert(requestDto));
    appUser.setEmail(shortUUID() + PLUS + appUser.getEmail());
    appUserService.saveUser(appUser);

    emailService.sendAccountWithdrawalMail(email, appUser.getFullName(), Languages.JAPANESE);
    emailService.sendAccountWithdrawalMailToAdmin(email, appUser.getSurnameOrderedName(),
        requestDto, Languages.JAPANESE);

    logout(appUser.getUserId());
  }

  private boolean incompleteOrdersByUserExists(Integer userId) {
    return orderService.existsByUserIdAndDeliveryTypeIn(userId, StatusConstants.ON_THE_WAY_ORDER_STATES_STRING);
  }

  private Page<ItemDocument> findOnSaleOrSoldItemsByUser(Integer userId) {
    ItemSearchRequest itemSearchRequest = ItemSearchRequest.builder()
        .notInStatuses(StatusConstants.SOLD_OR_CANCELED_STATUSES)
        .sellerId(userId)
        .build();
    return itemService.search(itemSearchRequest);
  }

  private String shortUUID() {
    UUID uuid = UUID.randomUUID();
    long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
    return Long.toString(l, Character.MAX_RADIX);
  }
}
