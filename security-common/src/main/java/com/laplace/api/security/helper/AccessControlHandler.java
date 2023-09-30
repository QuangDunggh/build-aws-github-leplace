package com.laplace.api.security.helper;

import com.laplace.api.security.marker.AccessControl;
import com.laplace.api.security.service.PermissionService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessControlHandler {

  private final PermissionService permissionService;

  @Autowired
  public AccessControlHandler(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @Before("execution(* com.laplace.api.cms..*.*(..)) && @annotation(accessControl)")
  public void checkEntityPermission(AccessControl accessControl) {
    if (!permissionService.hasEntityPermission(accessControl.value())) {
      throw new AccessDeniedException("User has no permission on this entity");
    }
  }
}
