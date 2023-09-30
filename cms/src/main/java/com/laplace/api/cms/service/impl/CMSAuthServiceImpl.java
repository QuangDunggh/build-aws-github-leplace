package com.laplace.api.cms.service.impl;

import com.laplace.api.cms.core.bean.ChangePasswordRequest;
import com.laplace.api.cms.core.bean.LoginRequestDto;
import com.laplace.api.cms.core.bean.SignUpRequest;
import com.laplace.api.cms.core.dto.LogInResponseDto;
import com.laplace.api.cms.service.CMSAuthService;
import com.laplace.api.cms.service.CMSEmailService;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.VerificationTokenType;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.request.ForgotPasswordRequest;
import com.laplace.api.common.dto.request.ResetPasswordRequest;
import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.CMSUserProfileModel;
import com.laplace.api.common.model.db.Role;
import com.laplace.api.common.model.db.VerificationToken;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.service.AdminUserService;
import com.laplace.api.common.service.TokenService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.security.auth.jwt.extractor.TokenExtractor;
import com.laplace.api.security.exceptions.InvalidJwtToken;
import com.laplace.api.security.model.token.TokenDTO;
import com.laplace.api.security.service.JwtTokenService;
import com.laplace.api.security.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
@Slf4j
public class CMSAuthServiceImpl implements CMSAuthService {

  private final AdminUserService adminUserService;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;
  private final VerificationTokenService verificationTokenService;
  private final CMSEmailService cmsEmailService;
  private final TokenService tokenService;
  private final TokenExtractor tokenExtractor;

  @Autowired
  public CMSAuthServiceImpl(
      PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService,
      AdminUserService adminUserService, VerificationTokenService verificationTokenService,
      CMSEmailService cmsEmailService,
      TokenService tokenService, TokenExtractor tokenExtractor) {
    this.passwordEncoder = passwordEncoder;
    this.adminUserService = adminUserService;
    this.jwtTokenService = jwtTokenService;
    this.verificationTokenService = verificationTokenService;
    this.cmsEmailService = cmsEmailService;
    this.tokenService = tokenService;
    this.tokenExtractor = tokenExtractor;
  }

  @Override
  public LogInResponseDto login(LoginRequestDto loginRequestDto) {
    Optional<AdminUser> userModel = adminUserService.findByEmail(loginRequestDto.getEmail());
    AdminUser user = userModel
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.AUTH_FAILURE));

    if (!user.isActive()) {
      throwApplicationException(ResultCodeConstants.AUTH_FAILURE);
    }

    if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
      throwApplicationException(ResultCodeConstants.AUTH_FAILURE);
    }

    TokenDTO tokenDTO = jwtTokenService.getTokens(user, AppType.CMS);

    Supplier<Stream<Role>> roleStream = () -> user.getUserRoles().stream()
        .map(userRole -> userRole.getId().getRole())
        .collect(Collectors.toSet())
        .stream();

    LogInResponseDto responseDto = new LogInResponseDto();
    responseDto.setAccessToken(tokenDTO.getAccessToken().getToken());
    responseDto.setRole(roleStream.get().map(Role::getRoleName).collect(Collectors.toSet()));

    //TODO: find out menu entity permission is necessary or not
    return responseDto;
  }

  @Override
  public void generateResetPasswordTokenAndSendMail(ForgotPasswordRequest requestDto) {
    AdminUser adminUser = this.adminUserService.findByEmail(requestDto.getEmail())
        .filter(AdminUser::isActive)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_ACTIVE));

    VerificationToken token = verificationTokenService
        .generateResetPasswordToken(adminUser.getUserId(), AppType.CMS,
            ApplicationConstants.VerificationTokenType.RESET_PASSWORD);
    cmsEmailService.sendResetPasswordMail(adminUser, token, requestDto.getLang());
  }

  @Transactional
  @Override
  public void resetPassword(ResetPasswordRequest request) {
    Integer userId = getUserIdFromToken(request.getOtp());

    AdminUser adminUser = adminUserService.findById(userId)
        .filter(AdminUser::isActive)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_EXISTS));

    savePassword(adminUser, request.getNewPassword(), userId);
    verificationTokenService.deleteToken(request.getOtp());

    List<Token> tokenList = tokenService.findByUserId(userId).stream().filter(token ->
        token.getAppType() == AppType.CMS).collect(Collectors.toList());
    tokenService.deleteAll(tokenList);
  }

  @Transactional
  @Override
  public void signUp(SignUpRequest request) {
    Integer userId = getUserIdFromToken(request.getToken());

    AdminUser adminUser = adminUserService.findById(userId)
        .filter(user -> !user.isActive() && user.getEmail().equalsIgnoreCase(request.getEmail()))
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.ALREADY_REGISTER));

    // create a default profile for user
    CMSUserProfileModel profile = new CMSUserProfileModel();
    profile.setUserId(adminUser.getUserId());
    profile.setUser(adminUser);
    adminUser.setProfile(profile);

    adminUser.setActive(true);
    savePassword(adminUser, request.getPassword(), userId);
    verificationTokenService.deleteToken(request.getToken());
  }

  @Transactional
  @Override
  public void changePassword(ChangePasswordRequest request, Integer userId) {
    if (!request.getOldPassword().equals(request.getNewPassword())
        && request.getNewPassword().equals(request.getVerifyPassword())) {

      AdminUser adminUser = adminUserService.findById(userId)
          .filter(AdminUser::isActive)
          .orElseThrow(() -> throwApplicationException(ResultCodeConstants.USER_NOT_EXISTS));

      if (passwordEncoder.matches(request.getOldPassword(), adminUser.getPassword())) {
        savePassword(adminUser, request.getNewPassword(), userId);
        List<Token> tokenList = tokenService.findByUserId(userId).stream().filter(token ->
            token.getAppType() == AppType.CMS).collect(Collectors.toList());
        tokenService.deleteAll(tokenList);
      } else {
        throwApplicationException(ResultCodeConstants.OLD_PASSWORD_NOT_MATCHED);
      }
    }
  }

  @Override
  public void logout(String token) {
    if (token != null && !token.isEmpty()) {
      String tokenValue = tokenExtractor.extract(token);
      Optional<Token> tokenOptional = tokenService.findByToken(tokenValue);
      if (!tokenOptional.isPresent()) {
        throw new InvalidJwtToken("Invalid Payload");
      }

      tokenService.delete(tokenOptional.get());
    }
  }

  private Integer getUserIdFromToken(String token) {
    return verificationTokenService
        .verifyToken(token, AppType.CMS, VerificationTokenType.RESET_PASSWORD)
        .map(VerificationToken::getUserId)
        .orElseThrow(() -> throwApplicationException(ResultCodeConstants.INVALID_TOKEN));
  }

  private void savePassword(AdminUser adminUser, String password, Integer updatedUserId) {
    adminUser.setPassword(passwordEncoder.encode(password));
    adminUser.setLastUpdatedBy(updatedUserId);
    adminUser.setLastUpdatedOn(DateUtil.timeNow());
    adminUserService.save(adminUser);
  }
}