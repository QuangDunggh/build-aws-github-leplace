package com.laplace.api.security.service.impl;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.AuthenticationType;
import com.laplace.api.common.constants.enums.Role;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.service.TokenService;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.config.JwtSettings;
import com.laplace.api.security.model.token.JwtToken;
import com.laplace.api.security.model.token.JwtTokenFactory;
import com.laplace.api.security.model.token.TokenDTO;
import com.laplace.api.security.service.JwtTokenService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

  private JwtTokenFactory jwtTokenFactory;
  private JwtSettings jwtSettings;
  private TokenService tokenService;

  @Autowired
  public JwtTokenServiceImpl(
      JwtTokenFactory jwtTokenFactory,
      JwtSettings jwtSettings,
      TokenService tokenService
  ) {
    this.jwtTokenFactory = jwtTokenFactory;
    this.jwtSettings = jwtSettings;
    this.tokenService = tokenService;
  }

  @Override
  public JwtToken getAccessToken(UserContext context) {
    return jwtTokenFactory.createAccessJwtToken(context);
  }

  @Override
  public TokenDTO getTokens(AdminUser user, AppType appType) {
    return getToken(getUserContext(user), user.getUserId(), appType);
  }

  @Override
  public TokenDTO getTokens(AppUser user, Integer authenticationType, AppType appType) {
    return getToken(getUserContext(user, authenticationType), user.getUserId(), appType);
  }

  private TokenDTO getToken(UserContext userContext, Integer userId, AppType appType) {
    JwtToken accessToken = getAccessToken(userContext);
    Token token = new Token();
    token.setJwtToken(accessToken.getToken());
    token.setExpiredAfter(jwtSettings.getTokenExpirationTime());
    token.setCreatedAt(new Date());
    token.setUserId(userId);
    token.setId(accessToken.getToken());
    token.setIsActive(userContext.isActive());
    token.setProfileComplete(userContext.isProfileComplete());
    token.setAppType(appType);
    tokenService.save(token);

    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setAccessToken(accessToken);
    return tokenDTO;
  }

  private UserContext getUserContext(AdminUser user) {
    List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
        .map(userRole -> new SimpleGrantedAuthority(
            getAuthority(userRole.getId().getRole().getRoleName())))
        .collect(Collectors.toList());
    return UserContext
        .create(user.getEmail(), user.getUserId(), user.getAccessId(), grantedAuthorities,
            user.isActive(), Boolean.TRUE, StringUtils.EMPTY_STRING, AuthenticationType.BASIC
                .getValue(), AppType.CMS);
  }

  private UserContext getUserContext(AppUser user,
      Integer authenticationType) {
    List<GrantedAuthority> grantedAuthorities = Collections
            .singletonList(new SimpleGrantedAuthority(
                    getAuthority(user.getVerificationStatus().equals(VerificationStatus.VERIFIED) ?
                            Role.SELLER.name() : Role.USER.name())));
    return UserContext
        .create(user.getEmail(), user.getUserId(), user.getAccessId(), grantedAuthorities,
            user.isActive(), BooleanUtils.isTrue(user.getProfileComplete()),
            user.getVerificationStatus().name(), authenticationType, AppType.WEB_CLIENT);
  }

  private String getAuthority(String roleName) {
    return ApplicationConstants.ROLE_PREFIX + roleName;
  }
}
