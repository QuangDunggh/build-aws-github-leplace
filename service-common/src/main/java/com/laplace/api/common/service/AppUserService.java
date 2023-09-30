package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.dto.response.projection.UnreadCountProjection;
import com.laplace.api.common.model.db.AppUser;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

  Optional<AppUser> findByEmail(String email);

  Optional<AppUser> findById(Integer userId);

  Optional<String> findEmailById(Integer userId);

  void saveUser(AppUser user);

  Optional<AppUser> findByFacebookId(String facebookId);

  Optional<AppUser> findByTwitterId(String twitterId);

  Page<AppUser> findUserByKeywordAndVerificationStatus(String keyword, List<VerificationStatus> verificationStatus, Pageable request);

  List<AppUser> findByIds(Iterable<Integer> ids);

  UnreadCountProjection findUnreadCountById(Integer userId);

  Map<Integer, String> findEmailMapByIds(Set<Integer> ids);

  Page<AppUser> findBestSellers(Pageable request);

  void incrementItemSentToLaPlaceCount(Integer userId, Integer counter);

  boolean existsByFacebookUserId(String facebookId);

  void incrementItemsOnDisplayCount(Integer sellerId);

  void updateCountOnPurchase(Integer sellerId);

  void decrementItemsOnDisplayCount(Integer sellerId);

  void incrementItemsSoldCount(Integer sellerId);

  void incrementUnreadCount(Integer userId);

  void decrementUnreadCount(Integer userId);

  boolean existsByEmailAndActiveTrue(String email);


}
