package com.laplace.api.web.service;

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
public class WMCBrandsService {

  private final BrandsService brandsService;

  @Autowired
  public WMCBrandsService(BrandsService brandsService) {
    this.brandsService = brandsService;
  }

  public List<BrandResponseDTO> getAllBrands() {
    return brandsService.getAllBrandsForUser();
  }

  public PageableResponseDTO<BrandResponseDTO> getPopularBrands(Pageable pageable) {
    return brandsService.getPopularBrandsForUser(pageable);
  }

  public BrandResponseDTO getBrandsById(Integer brandId) {
    return brandsService.getBrandsByIdForUser(brandId);
  }
}
