package com.laplace.api.common.service;

import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.SellerProfile;
import java.util.Optional;
import java.util.Set;

public interface SellerProfileService {

  Optional<SellerProfile> findById(Integer userId);

  void save(SellerProfile sellerProfile);

  void updateSNS(AppUser appUser);

  Set<SellerProfile> findByIdIn(Set<Integer> id);
}
