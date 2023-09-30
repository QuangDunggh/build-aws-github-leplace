package com.laplace.api.security.auth;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.config.JwtSettings;
import com.laplace.api.security.model.token.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtSettings jwtSettings;

  @Autowired
  public JwtAuthenticationProvider(JwtSettings jwtSettings) {
    this.jwtSettings = jwtSettings;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

    Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
    String subject = jwsClaims.getBody().getSubject();
    String id = jwsClaims.getBody().getId();
    List<String> scopes = jwsClaims.getBody().get("authorities", List.class);
    String accessId = jwsClaims.getBody().get("accessId", String.class);
    Boolean isActive = jwsClaims.getBody().get("isActive", Boolean.class);
    Boolean profileComplete = jwsClaims.getBody().get("profileComplete", Boolean.class);
    String verificationStatus = jwsClaims.getBody().get("verificationStatus", String.class);
    Integer authenticationType = jwsClaims.getBody().get("authenticationType", Integer.class);
    List<GrantedAuthority> authorities = scopes.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    Integer appType = jwsClaims.getBody().get("app", Integer.class);

    UserContext context = UserContext
        .create(subject, Integer.parseInt(id), accessId, authorities, isActive, profileComplete,
            verificationStatus, authenticationType, AppType.forValue(appType));

    return new JwtAuthenticationToken(context, context.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
