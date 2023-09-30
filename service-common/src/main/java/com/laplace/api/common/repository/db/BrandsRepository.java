package com.laplace.api.common.repository.db;

import com.laplace.api.common.model.db.Brand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsRepository extends PagingAndSortingRepository<Brand, Integer> {

  Optional<Brand> findByBrandIdAndIsVisibleIsTrue(Integer brandId);

  List<Brand> findAllByOrderByBrandName();

  List<Brand> findByIsVisibleIsTrueOrderByBrandName();

  Page<Brand> findByIsPopular(Boolean isPopular, Pageable pageable);

  Page<Brand> findByIsPopularAndIsVisibleIsTrue(Boolean isPopular, Pageable pageable);

  List<Brand> findAllByIsPopularIsTrue();

  Optional<Brand> findByBrandName(String brandName);

  Optional<Brand> findByBrandIdNotAndBrandName(Integer brandId, String brandName);
}
