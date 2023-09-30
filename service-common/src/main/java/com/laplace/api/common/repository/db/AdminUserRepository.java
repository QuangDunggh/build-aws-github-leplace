package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.UserRole;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends PagingAndSortingRepository<AdminUser, Integer> {

  Optional<AdminUser> findByEmail(String email);

  Page<AdminUser> findAdminUsersByUserRolesInAndActiveTrue(Set<UserRole> userRoles,
      Pageable pageRequest);

  List<AdminUser> findAllByEmailIn(Collection<String> email);
}
