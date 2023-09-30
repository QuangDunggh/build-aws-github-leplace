package com.laplace.api.common.service.cache;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.Role;
import com.laplace.api.common.model.db.UserRole;
import com.laplace.api.common.repository.db.UserRoleRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserIDCache {

  private Map<String, Integer> userIdMap;
  private UserRoleRepository userRoleRepository;

  @Autowired
  public UserIDCache(UserRoleRepository userRoleRepository) {
    this.userRoleRepository = userRoleRepository;
    userIdMap = new HashMap<>();
  }

  @PostConstruct
  public void init() {
    Optional<UserRole> userRole = userRoleRepository
        .findByIdRoleRoleId(Role.SUPER_ADMIN.getValue());

    if (userRole.isPresent()) {
      Integer userId = userRole.get().getId().getAdminUser().getUserId();
      userIdMap.put(ApplicationConstants.SUPER_ADMIN_USER_ID, userId);
      log.info("Admin User Id : " + userId);
    } else {
      //TODO: have to check
//            LaplaceResponseUtil.throwApplicationException(ResultCodeConstants.ROLE_NOT_FOUND);
    }
  }

  public Integer getSuperAdminUserId() {
    return userIdMap.get(ApplicationConstants.SUPER_ADMIN_USER_ID);
  }
}
