package com.laplace.api.common.service.impl;

import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.Role;
import com.laplace.api.common.model.db.UserRole;
import com.laplace.api.common.repository.db.AdminUserRepository;
import com.laplace.api.common.repository.db.RoleRepository;
import com.laplace.api.common.service.AdminUserService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

  private final AdminUserRepository adminUserRepository;
  private final RoleRepository roleRepository;

  @Autowired
  public AdminUserServiceImpl(AdminUserRepository adminUserRepository,
      RoleRepository roleRepository) {
    this.adminUserRepository = adminUserRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public Optional<AdminUser> findByEmail(String email) {
    return adminUserRepository.findByEmail(email);
  }

  @Override
  public Optional<AdminUser> findById(Integer userId) {
    return adminUserRepository.findById(userId);
  }

  @Override
  public void save(AdminUser adminUser) {
    adminUserRepository.save(adminUser);
  }

  @Transactional
  @Override
  public void addNewUser(AdminUser user, com.laplace.api.common.constants.enums.Role role) {
    Role roleEntity = roleRepository.findById(role.getValue())
        .orElseThrow(IllegalArgumentException::new);
    user.addRole(roleEntity);
    save(user);
  }

  @Override
  public Page<AdminUser> getUsersByRole(com.laplace.api.common.constants.enums.Role role,
      PageRequest request) {
    Set<UserRole> userRoles = roleRepository.findById(role.getValue())
        .map(Role::getUserRoles)
        .orElse(Collections.emptySet());
    return adminUserRepository.findAdminUsersByUserRolesInAndActiveTrue(userRoles, request);
  }

  @Override
  public List<AdminUser> findUsersByEmail(Collection<String> emails) {
    return adminUserRepository.findAllByEmailIn(emails);
  }

}
