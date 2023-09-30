package com.laplace.api.security.service;


import com.laplace.api.common.constants.enums.EntityType;

public interface PermissionService {

  /**
   * Check if a logged in user has permission of these menu entities
   *
   * @param entityType
   * @return
   */
  boolean hasEntityPermission(EntityType[] entityType);
}
