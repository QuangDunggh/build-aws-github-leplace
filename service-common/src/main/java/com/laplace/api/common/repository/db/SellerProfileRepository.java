package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.SellerProfile;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerProfileRepository extends JpaRepository<SellerProfile, Integer> {

  Set<SellerProfile> findBySellerIdIn(Set<Integer> ids);
}
