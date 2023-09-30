package com.laplace.api.common.repository.db;

import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.dto.response.projection.IdAndEmailProjection;
import com.laplace.api.common.dto.response.projection.UnreadCountProjection;
import com.laplace.api.common.model.db.AppUser;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

  Optional<AppUser> findByEmail(String email);

  @Query("SELECT u.email from AppUser u where u.userId=:userId")
  Optional<String> findEmailById(@Param("userId") Integer userId);

  Optional<AppUser> findByFacebookUserId(String facebookId);

  Optional<AppUser> findByTwitterUserId(String twitterUserID);

  Page<AppUser> findBySearchKeywordIgnoreCaseContainingAndVerificationStatusInAndActiveTrue(
      String keyword, List<VerificationStatus> verificationStatus,
      Pageable request);

  Page<AppUser> findBySearchKeywordIgnoreCaseContainingAndActiveTrue(
      @Param("keyword") String keyword, Pageable request);

  Page<AppUser> findByVerificationStatusInAndActiveTrue(List<VerificationStatus> verificationStatus,
      Pageable request);

  Page<AppUser> findAllByActiveTrue(Pageable request);

  List<AppUser> findAllByUserIdIn(Iterable<Integer> ids);

  List<IdAndEmailProjection> findAllByUserIdIn(Set<Integer> ids);

  Optional<UnreadCountProjection> findUnreadCountByUserId(Integer id);

  Page<AppUser> findByUserStatusAndAccountWithdrawnAndVerificationStatusNot(UserStatus userStatus,
      boolean accountWithdrawn,
      VerificationStatus verificationStatus, Pageable request);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET items_sent_to_laplace =  Coalesce(items_sent_to_laplace, 0) + :counter WHERE user_id = :id", nativeQuery = true)
  void incrementItemSentToLaPlaceCount(@Param("id") Integer id, @Param("counter") Integer counter);

  boolean existsByFacebookUserId(String facebookId);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET items_on_display =  Coalesce(items_on_display, 0) + 1 WHERE user_id = :id", nativeQuery = true)
  void incrementItemsOnDisplayCount(@Param("id") Integer id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET items_on_display =  items_on_display - 1 WHERE user_id = :id", nativeQuery = true)
  void decrementItemsOnDisplayCount(@Param("id") Integer id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET items_sold =  Coalesce(items_sold, 0) + 1 WHERE user_id = :id", nativeQuery = true)
  void incrementItemsSoldCount(@Param("id") Integer id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET items_sold =  Coalesce(items_sold, 0) + 1, items_on_display =  items_on_display - 1 WHERE user_id = :id", nativeQuery = true)
  void updateCountOnPurchase(@Param("id") Integer id);

  boolean existsByEmailAndActiveTrue(String email);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET unread_count =  Coalesce(unread_count, 0) + 1 WHERE user_id = :userId", nativeQuery = true)
  void incrementUnreadCount(@Param("userId") Integer userId);

  @Modifying
  @Transactional
  @Query(value = "UPDATE app_users SET unread_count =  unread_count - 1 WHERE user_id = :userId", nativeQuery = true)
  void decrementUnreadCount(@Param("userId") Integer userId);
}
