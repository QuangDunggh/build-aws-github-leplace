package com.laplace.api.security.service;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.util.UserContext;
import com.laplace.api.security.model.token.JwtToken;
import com.laplace.api.security.model.token.TokenDTO;

public interface JwtTokenService {

  JwtToken getAccessToken(UserContext context);

  TokenDTO getTokens(AdminUser user, AppType appType);

  TokenDTO getTokens(AppUser user, Integer authenticationType, AppType appType);
}
