package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.Role;
import com.laplace.api.common.model.db.AdminUser;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AdminUserService {

  Optional<AdminUser> findByEmail(String email);

  Optional<AdminUser> findById(Integer userId);

  void save(AdminUser adminUser);

  void addNewUser(AdminUser user, Role role);

  Page<AdminUser> getUsersByRole(Role role, PageRequest request);

  List<AdminUser> findUsersByEmail(Collection<String> emails);
}
