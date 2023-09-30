package com.laplace.api.security.model.token;

import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.config.JwtSettings;
import com.laplace.api.security.model.Scopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ToString
@Slf4j
public class JwtTokenFactory {

  private final JwtSettings settings;

  @Autowired
  public JwtTokenFactory(JwtSettings settings) {
    this.settings = settings;
  }

  public AccessJwtToken createAccessJwtToken(UserContext userContext) {

    if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty()) {
      throw new IllegalArgumentException("User doesn't have any privileges");
    }

    Claims claims = Jwts.claims().setSubject(userContext.getEmail());
    claims.setId(String.valueOf(userContext.getUserId()));
    claims.put("authorities", userContext.getAuthorities()
        .stream().map(Object::toString).collect(Collectors.toList()));
    claims.put("accessId", userContext.getAccessId());
    claims.put("app",userContext.getAppType().getValue());
    claims.put("isActive", userContext.isActive());
    claims.put("profileComplete", userContext.isProfileComplete());
    claims.put("verificationStatus", userContext.getVerificationStatus());
    claims.put("authenticationType", userContext.getAuthenticationType());
    ZonedDateTime currentTime = DateUtil.timeNow();

    log.debug("Access token claims info. claims={}", claims.toString());
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setIssuedAt(Date.from(currentTime.toInstant()))
        .setExpiration(Date.from(currentTime
            .plusSeconds(settings.getTokenExpirationTime())
            .toInstant()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();

    return new AccessJwtToken(token, claims);
  }

  public JwtToken createRefreshToken(UserContext userContext) {
    if (StringUtils.isBlank(userContext.getEmail())) {
      throw new IllegalArgumentException("Cannot create JWT Token without username");
    }

    ZonedDateTime currentTime = DateUtil.timeNow();

    Claims claims = Jwts.claims().setSubject(userContext.getEmail());
    claims.put("scopes", Collections.singletonList(Scopes.REFRESH_TOKEN.authority()));

    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuer(settings.getTokenIssuer())
        .setId(settings.getRefreshTokenSalt())
        .setIssuedAt(Date.from(currentTime.toInstant()))
        .setExpiration(Date.from(currentTime
            .plusSeconds(settings.getRefreshTokenExpTime())
            .toInstant()))
        .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();

    return new AccessJwtToken(token, claims);
  }
}
