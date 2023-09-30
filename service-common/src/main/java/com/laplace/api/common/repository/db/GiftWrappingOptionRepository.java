package com.laplace.api.common.repository.db;

import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.model.db.GiftWrappingOption;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftWrappingOptionRepository extends CrudRepository<GiftWrappingOption, Integer> {

  Optional<GiftWrappingOption> findByGiftWrappingType(GiftWrappingType giftWrappingType);
}
