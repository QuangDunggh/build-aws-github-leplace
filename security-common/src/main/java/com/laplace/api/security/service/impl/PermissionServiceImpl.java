package com.laplace.api.security.service.impl;

import com.laplace.api.common.constants.enums.EntityType;
import com.laplace.api.common.repository.db.RoleRepository;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.security.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

  private final AuthenticationFacade authenticationFacade;
  private final RoleRepository roleRepository;

  @Autowired
  public PermissionServiceImpl(AuthenticationFacade authenticationFacade,
      RoleRepository roleRepository) {
    this.authenticationFacade = authenticationFacade;
    this.roleRepository = roleRepository;
  }

  @Override
  public boolean hasEntityPermission(EntityType[] entityType) {
    return true;
  }
}
