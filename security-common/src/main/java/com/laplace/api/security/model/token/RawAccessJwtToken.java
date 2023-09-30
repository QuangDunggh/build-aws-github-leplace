package com.laplace.api.security.model.token;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.logging.LogMessageConfig;
import com.laplace.api.security.exceptions.InvalidJwtToken;
import com.laplace.api.security.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

public class RawAccessJwtToken implements JwtToken {

  private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

  private String token;

  private LogMessageConfig logMessageConfig;

  public RawAccessJwtToken(String token) {
    this.token = token;
    logMessageConfig = LogMessageConfig.getApplicationContext().getBean(LogMessageConfig.class);
  }

  /**
   * Parses and validates JWT Token signature.
   *
   * @throws BadCredentialsException
   * @throws JwtExpiredTokenException
   */
  public Jws<Claims> parseClaims(String signingKey) {
    try {
      return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
    } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
      logger.error("Invalid JWT Token", ex);
      throw new InvalidJwtToken(
          logMessageConfig.getErrorMessageInfo(ErrorCode.INVALID_JWT_TOKEN).getMessageTemplate());
    } catch (ExpiredJwtException expiredEx) {
      logger.info("JWT Token is expired", expiredEx);
      throw new JwtExpiredTokenException(this,
          logMessageConfig.getErrorMessageInfo(ErrorCode.JWT_TOKEN_EXPIRED).getMessageTemplate(),
          expiredEx);
    }
  }

  @Override
  public String getToken() {
    return token;
  }
}
