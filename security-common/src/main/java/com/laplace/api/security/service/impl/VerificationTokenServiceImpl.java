package com.laplace.api.security.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.returnApplicationException;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.AuthenticationType;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.model.db.VerificationToken;
import com.laplace.api.common.repository.db.VerificationTokenRepository;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.security.service.VerificationTokenService;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {

  private static final int KEY_BIT_SIZE = 512;
  private static final String OTP_FORMAT = "%06d";
  private final VerificationTokenRepository verificationTokenRepository;
  private final MailConfiguration mailConfiguration;
  private final TimeBasedOneTimePasswordGenerator totp;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository,
      MailConfiguration mailConfiguration,
      AuthenticationFacade authenticationFacade) throws NoSuchAlgorithmException {
    this.verificationTokenRepository = verificationTokenRepository;
    this.mailConfiguration = mailConfiguration;
    this.authenticationFacade = authenticationFacade;
    this.totp = new TimeBasedOneTimePasswordGenerator();
  }

  @Override
  public VerificationToken generateResetPasswordToken(Integer userId, AppType appType,
      Integer tokenType) {
    ZonedDateTime expirationTime = DateUtil.timeNow()
        .plusHours(mailConfiguration.getResetPasswordMailExpiration());
    return getVerificationToken(userId, appType, tokenType, expirationTime);
  }

  @Override
  public Optional<VerificationToken> verifyToken(String token, AppType appType, Integer tokenType) {
    return verificationTokenRepository.findById(token)
        .filter(vToken -> NumberUtils.compare(appType.getValue(), vToken.getAppType()) == 0)
        .filter(vToken -> NumberUtils.compare(tokenType, vToken.getTokenType()) == 0)
        .flatMap(this::verify);
  }

  @Override
  public Optional<VerificationToken> verifyToken(String token, AppType appType,
      Set<Integer> tokenTypes) {
    return verificationTokenRepository.findById(token)
        .filter(vToken -> NumberUtils.compare(appType.getValue(), vToken.getAppType()) == 0)
        .filter(vToken -> CollectionUtils.isNotEmpty(tokenTypes) && tokenTypes
            .contains(vToken.getTokenType()))
        .flatMap(this::verify);
  }

  @Override
  public void deleteToken(String token) {
    verificationTokenRepository.deleteByToken(token);
  }

  @Override
  public VerificationToken generateInvitationToken(Integer userId, AppType appType,
      Integer tokenType) {
    ZonedDateTime expirationTime = DateUtil.timeNow()
        .plusHours(mailConfiguration.getInvitationMailExpiration());
    return getVerificationToken(userId, appType, tokenType, expirationTime);
  }

  @Override
  public VerificationToken generateOTP(Integer userId, AppType appType, Integer tokenType,
      String context) {
    ZonedDateTime expirationTime = DateUtil.timeNow()
        .plusHours(mailConfiguration.getOtpExpiration());
    long expirationTimeInMillis = expirationTime.toInstant().toEpochMilli();
    VerificationToken verificationToken = verificationTokenRepository
        .findAllByUserIdAndAppTypeAndTokenType(userId, appType.getValue(), tokenType)
        .stream().findFirst()
        .orElse(
            buildToken(userId, appType, tokenType, expirationTimeInMillis, generateOTP(), context));
    verificationToken.setExpirartionTime(expirationTimeInMillis);
    verificationToken.setContext(context);
    verificationTokenRepository.save(verificationToken);
    return verificationToken;
  }

  private VerificationToken getVerificationToken(Integer userId, AppType appType, Integer tokenType,
      ZonedDateTime expirationTime) {
    long expirationTimeInMillis = DateUtil.toEpochMilli(expirationTime);
    VerificationToken verificationToken = buildToken(userId, appType, tokenType,
        expirationTimeInMillis, DateUtil.getUniqueTimeBasedUUID(), null);
    verificationTokenRepository.save(verificationToken);
    return verificationToken;
  }

  private boolean isTimeExpired(Long expirationTime) {
    return System.currentTimeMillis() > expirationTime;
  }

  private Optional<VerificationToken> verify(VerificationToken tokenEntity) {
    boolean timeExpired = isTimeExpired(tokenEntity.getExpirartionTime());
    if (timeExpired) {
      deleteToken(tokenEntity.getToken()); // removing expired token from DB
      return Optional.empty();
    } else {
      return Optional.of(tokenEntity);
    }
  }

  private VerificationToken buildToken(Integer userId, AppType appType, Integer tokenType,
      Long expirationTime, String token, String context) {
    return VerificationToken.builder()
        .userId(userId)
        .token(token)
        .appType(appType.getValue())
        .createdOn(DateUtil.timeNow())
        .expirartionTime(expirationTime)
        .tokenType(tokenType)
        .context(context)
        .authenticationType(getAuthenticationType())
        .build();
  }

  private String generateOTP() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
      keyGenerator.init(KEY_BIT_SIZE);
      Key key = keyGenerator.generateKey();
      return String.format(OTP_FORMAT, totp.generateOneTimePassword(key, Instant.now()));
    } catch (NoSuchAlgorithmException e) {
      log.error("KeyGenerator Error:", e);
      throw returnApplicationException(ResultCodeConstants.INTERNAL_SERVER_ERROR);
    } catch (InvalidKeyException e) {
      log.error("Invalid Key Error:", e);
      throw returnApplicationException(ResultCodeConstants.INTERNAL_SERVER_ERROR);
    }
  }

  private Integer getAuthenticationType() {
    return authenticationFacade.getUserContext().map(UserContext::getAuthenticationType).orElse(
        AuthenticationType.BASIC.getValue());
  }
}
