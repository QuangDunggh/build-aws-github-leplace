package com.laplace.api.security.helper;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.auth.JwtAuthenticationToken;
import com.laplace.api.security.config.SecurityConfigConstants;
import com.laplace.api.security.exceptions.AuthMethodNotSupportedException;
import com.laplace.api.security.exceptions.InvalidJwtToken;
import com.laplace.api.security.model.token.RawAccessJwtToken;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public interface AuthenticationFacade {

  Authentication getAuthentication();

  default Optional<UserContext> getUserContext() {
    return Optional.ofNullable(getAuthentication())
        .filter(auth -> auth instanceof JwtAuthenticationToken)
        .map(token -> (UserContext) token.getPrincipal());
  }

  default Collection<GrantedAuthority> getAuthorities() {
    return getUserContext().map(UserContext::getAuthorities)
        .orElse(Collections.emptyList());
  }

  default Set<String> getUserRoles() {
    return getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  default String getAccessId() {
    return getUserContext().map(UserContext::getAccessId).get();
  }

  default AppType getAppType() {
    return getUserContext().map(UserContext::getAppType).get();
  }

  default Integer getUserId() { return getUserContext().map(UserContext::getUserId).get(); }

  Integer getUserId(String token);
}
