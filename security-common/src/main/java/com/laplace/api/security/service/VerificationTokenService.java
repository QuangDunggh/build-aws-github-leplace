package com.laplace.api.security.service;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.model.db.VerificationToken;
import java.util.Optional;
import java.util.Set;

public interface VerificationTokenService {

  VerificationToken generateResetPasswordToken(Integer userId, AppType appType, Integer tokenType);

  /**
   * @param token generated token
   * @return If the token is valid return verify token else return empty
   */
  Optional<VerificationToken> verifyToken(String token, AppType appType, Integer tokenType);

  Optional<VerificationToken> verifyToken(String token, AppType appType, Set<Integer> tokenTypes);

  void deleteToken(String token);

  VerificationToken generateInvitationToken(Integer userId, AppType appType, Integer tokenType);

  VerificationToken generateOTP(Integer userId, AppType appType, Integer tokenType, String context);
}
