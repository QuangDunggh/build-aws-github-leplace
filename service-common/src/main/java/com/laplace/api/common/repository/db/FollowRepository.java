package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends CrudRepository<Follow, String> {

  Optional<Follow> findByUserIdAndSellerId(Integer userId, Integer sellerId);

  List<Follow> findAllByUserIdOrSellerId(Integer userId, Integer sellerId);

  List<Follow> findAllByUserId(Integer userId);

  List<Follow> findAllBySellerId(Integer sellerId);
}
