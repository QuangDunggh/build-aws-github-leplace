package com.laplace.api.cms.service.impl;

import com.laplace.api.cms.service.CMSBrandsService;
import com.laplace.api.common.dto.request.BrandRequestDTO;
import com.laplace.api.common.dto.response.BrandResponseDTO;
import com.laplace.api.common.service.BrandsService;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CMSBrandsServiceImpl implements CMSBrandsService {

  private final BrandsService brandsService;

  @Autowired
  public CMSBrandsServiceImpl(BrandsService brandsService) {
    this.brandsService = brandsService;
  }

  @Override
  public List<BrandResponseDTO> getAllBrands() {
    return brandsService.getAllBrands();
  }

  @Override
  public PageableResponseDTO<BrandResponseDTO> getPopularBrands(Pageable pageable) {
    return brandsService.getPopularBrands(pageable);
  }

  @Override
  public BrandResponseDTO getBrandsById(Integer brandId) {
    return brandsService.getBrandsById(brandId);
  }

  @Override
  public BrandResponseDTO addBrand(BrandRequestDTO request) {
    return brandsService.addBrand(request);
  }

  @Override
  public BrandResponseDTO updateBrand(Integer brandId, BrandRequestDTO request) {
    return brandsService.updateBrand(brandId, request);
  }
}
