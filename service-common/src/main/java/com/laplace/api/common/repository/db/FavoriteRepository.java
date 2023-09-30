package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.Favorite;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, String> {

  Optional<Favorite> findByUserIdAndItemId(Integer userId, String itemId);

  List<Favorite> findByUserIdOrderByCreatedOnDesc(Integer userId);

  Set<Favorite> findByUserIdAndItemIdIn(Integer userId, Set<String> itemIds);
}
