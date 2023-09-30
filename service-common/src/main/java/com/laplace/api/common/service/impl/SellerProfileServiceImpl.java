package com.laplace.api.common.service.impl;

import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.SellerProfile;
import com.laplace.api.common.repository.db.SellerProfileRepository;
import com.laplace.api.common.service.SellerProfileService;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SellerProfileServiceImpl implements SellerProfileService {
  private final SellerProfileRepository sellerProfileRepository;

  @Autowired
  public SellerProfileServiceImpl(
      SellerProfileRepository sellerProfileRepository) {
    this.sellerProfileRepository = sellerProfileRepository;
  }

  @Override
  public Optional<SellerProfile> findById(Integer userId) {
    return sellerProfileRepository.findById(userId);
  }

  @Override
  public void save(SellerProfile sellerProfile) {
    sellerProfileRepository.save(sellerProfile);
  }

  @Override
  public void updateSNS(AppUser appUser) {
    Optional<SellerProfile> profileOptional = findById(appUser.getUserId());
    if (profileOptional.isPresent()) {
      SellerProfile sellerProfile = profileOptional.get();
      sellerProfile.setShareFb(appUser.isFacebookEnable() && sellerProfile.getShareFb());
      sellerProfile.setShareTwitter(appUser.isTwitterEnable() && sellerProfile.getShareTwitter());
      save(sellerProfile);
    }
  }

  @Override
  public Set<SellerProfile> findByIdIn(Set<Integer> ids) {
    return sellerProfileRepository.findBySellerIdIn(ids);
  }
}
