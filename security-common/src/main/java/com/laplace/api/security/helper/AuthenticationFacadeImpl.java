package com.laplace.api.security.helper;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.repository.redis.TokenRepository;
import com.laplace.api.security.auth.jwt.extractor.JwtHeaderTokenExtractor;
import com.laplace.api.security.config.JwtSettings;
import com.laplace.api.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class AuthenticationFacadeImpl implements AuthenticationFacade {

  private final JwtHeaderTokenExtractor tokenExtractor;
  private final TokenRepository tokenRepository;
  private final JwtSettings jwtSettings;

  @Autowired
  public AuthenticationFacadeImpl(
      JwtHeaderTokenExtractor tokenExtractor,
      TokenRepository tokenRepository, JwtSettings jwtSettings) {
    this.tokenExtractor = tokenExtractor;
    this.tokenRepository = tokenRepository;
    this.jwtSettings = jwtSettings;
  }

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  public Integer getUserId(String token) {
    if (ObjectUtils.isEmpty(token)) {
      return ApplicationConstants.ANONYMOUS_USER;
    }
    try {
    String tokenValue = tokenExtractor.extract(token);
    Optional<Token> tokenOptional = tokenRepository.findById(tokenValue);
    if (tokenOptional.isEmpty()) {
      return ApplicationConstants.ANONYMOUS_USER;
    }
      Jws<Claims> claimsJws = new RawAccessJwtToken(tokenValue)
          .parseClaims(jwtSettings.getTokenSigningKey());
      return Integer.parseInt(claimsJws.getBody().getId());
    } catch (Exception exception) {
      log.error("Error while parsing token: " + exception.getMessage());
      return ApplicationConstants.ANONYMOUS_USER;
    }
  }
}
