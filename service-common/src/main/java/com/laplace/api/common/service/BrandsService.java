package com.laplace.api.common.service;

import com.laplace.api.common.dto.request.BrandRequestDTO;
import com.laplace.api.common.dto.response.BrandResponseDTO;
import com.laplace.api.common.model.db.Brand;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface BrandsService {

  List<BrandResponseDTO> getAllBrands();

  List<BrandResponseDTO> getAllBrandsForUser();

  PageableResponseDTO<BrandResponseDTO> getPopularBrands(Pageable pageable);

  PageableResponseDTO<BrandResponseDTO> getPopularBrandsForUser(Pageable pageable);

  BrandResponseDTO getBrandsById(Integer brandId);

  BrandResponseDTO getBrandsByIdForUser(Integer brandId);

  BrandResponseDTO addBrand(BrandRequestDTO request);

  BrandResponseDTO updateBrand(Integer brandId, BrandRequestDTO request);

  Optional<Brand> findById(Integer brandId);

  List<Brand> findAllPopularBrands();

}
