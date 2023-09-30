package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.UserRole;
import com.laplace.api.common.model.db.UserRoleId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

  Optional<UserRole> findByIdRoleRoleId(Integer roleId);

}
