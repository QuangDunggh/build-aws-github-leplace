package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.ItemPackage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPackageRepository extends PagingAndSortingRepository<ItemPackage, String> {

}
