package com.laplace.api.cms.service;

import com.laplace.api.common.dto.request.BrandRequestDTO;
import com.laplace.api.common.dto.response.BrandResponseDTO;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CMSBrandsService {

  List<BrandResponseDTO> getAllBrands();

  PageableResponseDTO<BrandResponseDTO> getPopularBrands(Pageable pageable);

  BrandResponseDTO getBrandsById(Integer brandId);

  BrandResponseDTO addBrand(BrandRequestDTO request);

  BrandResponseDTO updateBrand(Integer brandId, BrandRequestDTO request);
}
