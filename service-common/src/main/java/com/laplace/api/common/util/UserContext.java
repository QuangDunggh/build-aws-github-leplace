package com.laplace.api.common.util;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString
@AllArgsConstructor
public class UserContext {

  private final String email;
  private final Integer userId;
  private final String accessId;
  private final List<GrantedAuthority> authorities;
  private final boolean active;
  private final boolean profileComplete;
  private final String verificationStatus;
  private final Integer authenticationType;
  private final AppType appType;

  public static UserContext create(String email, Integer userId, String accessId,
      List<GrantedAuthority> authorities, boolean active, boolean profileComplete,
      String verificationStatus, Integer authenticationType, AppType appType) {
    if (authorities.stream().anyMatch(a -> !a.getAuthority()
        .equalsIgnoreCase(ApplicationConstants.ROLE_PREFIX + Role.USER.name()))
        && StringUtils.isBlank(email)) {
      throw new IllegalArgumentException("email is blank: " + email);
    }
    return new UserContext(email, userId, accessId, authorities, active, profileComplete,
        verificationStatus, authenticationType, appType);
  }

}
