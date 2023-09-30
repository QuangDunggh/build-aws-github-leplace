package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.AppUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserProfileRepository extends JpaRepository<AppUserProfile, Integer> {

}
