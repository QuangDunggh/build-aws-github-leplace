package com.laplace.api.common.service.impl;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.USER_NOT_EXISTS;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;
import static java.util.stream.Collectors.toMap;

import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.dto.response.projection.IdAndEmailProjection;
import com.laplace.api.common.dto.response.projection.UnreadCountProjection;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.repository.db.AppUserRepository;
import com.laplace.api.common.service.AppUserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class AppUserServiceImpl implements AppUserService {

  private final AppUserRepository appUserRepository;

  @Autowired
  public AppUserServiceImpl(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Override
  public Optional<AppUser> findByEmail(String email) {
    return appUserRepository.findByEmail(email);
  }

  @Override
  public Optional<AppUser> findById(Integer userId) {
    return appUserRepository.findById(userId);
  }

  @Override
  public Optional<String> findEmailById(Integer userId) {
    return appUserRepository.findEmailById(userId);
  }

  @Override
  public void saveUser(AppUser user) {
    appUserRepository.save(user);
  }

  @Override
  public Optional<AppUser> findByFacebookId(String facebookUserId) {
    return appUserRepository.findByFacebookUserId(facebookUserId);
  }

  @Override
  public Page<AppUser> findUserByKeywordAndVerificationStatus(String keyword, List<VerificationStatus> verificationStatus, Pageable request) {

    if (ObjectUtils.isEmpty(verificationStatus)) {
      return ObjectUtils.isEmpty(keyword) ? appUserRepository.findAllByActiveTrue(request) :
              appUserRepository.findBySearchKeywordIgnoreCaseContainingAndActiveTrue(keyword, request);
    } else {
      return ObjectUtils.isEmpty(keyword) ? appUserRepository
          .findByVerificationStatusInAndActiveTrue(verificationStatus, request) :
          appUserRepository.findBySearchKeywordIgnoreCaseContainingAndVerificationStatusInAndActiveTrue(keyword,
              verificationStatus, request);
    }
  }

  @Override
  public List<AppUser> findByIds(Iterable<Integer> ids) {
    return appUserRepository.findAllByUserIdIn(ids);
  }

  @Override
  public UnreadCountProjection findUnreadCountById(Integer userId) {
    return appUserRepository.findUnreadCountByUserId(userId).orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
  }

  @Override
  public Map<Integer, String> findEmailMapByIds(Set<Integer> ids) {
    return appUserRepository.findAllByUserIdIn(ids).stream().collect(toMap(IdAndEmailProjection::getUserId, IdAndEmailProjection::getEmail));
  }

  @Override
  public Page<AppUser> findBestSellers(Pageable request) {
    return appUserRepository
        .findByUserStatusAndAccountWithdrawnAndVerificationStatusNot(UserStatus.NORMAL, false,
            VerificationStatus.NOT_VERIFIED, request);
  }

  @Override
  public void incrementItemSentToLaPlaceCount(Integer userId, Integer counter) {
    appUserRepository.incrementItemSentToLaPlaceCount(userId, counter);
  }

  @Override
  public boolean existsByFacebookUserId(String facebookId) {
    return appUserRepository.existsByFacebookUserId(facebookId);
  }

  @Override
  public Optional<AppUser> findByTwitterId(String twitterId) {
    return appUserRepository.findByTwitterUserId(twitterId);
  }

  @Override
  public void incrementItemsOnDisplayCount(Integer sellerId) {
    appUserRepository.incrementItemsOnDisplayCount(sellerId);
  }

  @Override
  public void updateCountOnPurchase(Integer sellerId) {
    appUserRepository.updateCountOnPurchase(sellerId);
  }

  @Override
  public void decrementItemsOnDisplayCount(Integer sellerId) {
    appUserRepository.decrementItemsOnDisplayCount(sellerId);
  }

  @Override
  public void incrementItemsSoldCount(Integer sellerId) {
    appUserRepository.incrementItemsSoldCount(sellerId);
  }

  @Override
  public void incrementUnreadCount(Integer userId) {
    appUserRepository.incrementUnreadCount(userId);
  }

  @Override
  public void decrementUnreadCount(Integer userId) {
    appUserRepository.decrementUnreadCount(userId);
  }

  @Override
  public boolean existsByEmailAndActiveTrue(String email) {
    return appUserRepository.existsByEmailAndActiveTrue(email);
  }
}
