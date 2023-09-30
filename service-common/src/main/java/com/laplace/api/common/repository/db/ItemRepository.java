package com.laplace.api.common.repository.db;

import com.laplace.api.common.constants.enums.ItemStatus;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.model.db.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, String> {

  Page<Item> findByStatusIn(List<ItemStatus> itemStatus, Pageable request);

  List<Item> findAllBySellerId(Integer sellerId);

  Page<Item> findByPickUpIsTrueAndStatusInAndHiddenIsFalseAndBlacklistedIsFalse(
      Set<ItemStatus> statuses, Pageable page);

  Page<Item> findByStatusInAndHiddenIsFalseAndBlacklistedIsFalse(Set<ItemStatus> statuses,
      Pageable page);

  Page<Item> findAllByStatusInAndCreatedByAndHiddenIsFalseAndBlacklistedIsFalseOrderByCreatedOnDesc(
      Set<ItemStatus> itemStatuses, Integer createdBy, Pageable pageable);

  Page<Item> findAllByStatusInAndCreatedByAndHiddenIsFalseAndBlacklistedIsFalseOrderByDisplayRequestDateDesc(
      Set<ItemStatus> itemStatuses, Integer createdBy, Pageable pageable);

  Page<Item> findAllByCreatedByAndStatusInAndHiddenIsFalseAndBlacklistedIsFalseOrCreatedByAndJudgementStatusAndHiddenIsFalseAndBlacklistedIsFalseOrderByDisplayRequestDateDesc(
      Integer createdBy1, Set<ItemStatus> itemStatuses, Integer createdBy2,
      JudgementStatus judgementStatus, Pageable pageable);

  Page<Item> findAllByStatusInAndCreatedByOrJudgementStatusAndHiddenIsFalseAndBlacklistedIsFalse(
      Set<ItemStatus> itemStatuses, Integer createdBy, JudgementStatus judgementStatus,
      Pageable pageable);

  Page<Item> findByTargetAudienceInAndStatusInAndHiddenIsFalseAndBlacklistedIsFalse(
      Set<TargetAudience> targetAudienceSet, Set<ItemStatus> displayableStatuses, Pageable page);

  Page<Item> findAllByItemIdInAndHiddenIsFalseAndBlacklistedIsFalse(List<String> itemIds,
      Pageable pageable);

  Optional<Item> findByItemIdAndBlacklistedIsFalse(String id);

  @Transactional
  @Lock(LockModeType.OPTIMISTIC)
  Optional<Item> findByItemIdAndStatusInAndBlacklistedIsFalse(String id, Set<ItemStatus> statuses);

  @Modifying
  @Transactional
  @Query(value = "UPDATE items SET favourite_count = favourite_count - 1 WHERE item_id = :id", nativeQuery = true)
  void decrementFavCount(@Param("id") String id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE items SET favourite_count =  Coalesce(favourite_count, 0) + 1 WHERE item_id = :id", nativeQuery = true)
  void incrementFavCount(@Param("id") String id);

  @Modifying
  @Transactional
  @Query(value = "update items set blacklisted = true where seller_id= :sellerId", nativeQuery = true)
  void setBlacklistedBySellerId(@Param("sellerId") Integer sellerId);

  Set<Item> findAllByItemIdIn(Set<String> itemIds);

  @Query("SELECT i.coverImage from Item i where i.itemId=:itemId")
  String findCoverImageByItemId(@Param("itemId") String itemId);
}
