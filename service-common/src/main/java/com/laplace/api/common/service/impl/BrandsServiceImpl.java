package com.laplace.api.common.service.impl;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.BRAND_NAME_ALREADY_EXISTS;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.NOT_FOUND;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.converter.response.BrandResponseConverter;
import com.laplace.api.common.dto.request.BrandRequestDTO;
import com.laplace.api.common.dto.response.BrandResponseDTO;
import com.laplace.api.common.model.db.Brand;
import com.laplace.api.common.repository.db.BrandsRepository;
import com.laplace.api.common.service.BrandsService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PageableResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class BrandsServiceImpl implements BrandsService {

  private final BrandsRepository brandsRepository;
  private final BrandResponseConverter brandResponseConverter;

  @Autowired
  public BrandsServiceImpl(BrandsRepository brandsRepository,
      BrandResponseConverter brandResponseConverter) {
    this.brandsRepository = brandsRepository;
    this.brandResponseConverter = brandResponseConverter;
  }

  @Override
  public List<BrandResponseDTO> getAllBrands() {
    return brandsRepository.findAllByOrderByBrandName().stream()
        .map(brandResponseConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public List<BrandResponseDTO> getAllBrandsForUser() {
    return brandsRepository.findByIsVisibleIsTrueOrderByBrandName().stream()
        .map(brandResponseConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public PageableResponseDTO<BrandResponseDTO> getPopularBrands(Pageable pageable) {
    Page<Brand> popularBrands = brandsRepository.findByIsPopular(true, pageable);
    return PageableResponseDTO
        .create(popularBrands.getTotalElements(), popularBrands.getTotalPages(),
            popularBrands.stream().map(brandResponseConverter::convert)
                .collect(Collectors.toList()));
  }

  @Override
  public PageableResponseDTO<BrandResponseDTO> getPopularBrandsForUser(Pageable pageable) {
    Page<Brand> popularBrands = brandsRepository.findByIsPopularAndIsVisibleIsTrue(
        true, pageable);
    return PageableResponseDTO
        .create(popularBrands.getTotalElements(), popularBrands.getTotalPages(),
            popularBrands.stream().map(brandResponseConverter::convert)
                .collect(Collectors.toList()));
  }

  @Override
  public BrandResponseDTO getBrandsById(Integer brandId) {
    Brand brand = brandsRepository.findById(brandId)
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    return brandResponseConverter.convert(brand);
  }

  @Override
  public BrandResponseDTO getBrandsByIdForUser(Integer brandId) {
    Brand brand = brandsRepository.findByBrandIdAndIsVisibleIsTrue(brandId)
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    return brandResponseConverter.convert(brand);
  }

  @Override
  public BrandResponseDTO addBrand(BrandRequestDTO request) {
    if (brandsRepository.findByBrandName(request.getBrandName()).isPresent()) {
      throw throwApplicationException(BRAND_NAME_ALREADY_EXISTS);
    }
    Brand brand = new Brand();
    brand = setBrand(brand, request);
    brand.setCreatedBy(request.getUserId());
    brand.setCreatedOn(DateUtil.timeNow());
    brandsRepository.save(brand);
    return brandResponseConverter.convert(brand);
  }

  @Override
  public BrandResponseDTO updateBrand(Integer brandId, BrandRequestDTO request) {
    if (brandsRepository.findByBrandIdNotAndBrandName(brandId, request.getBrandName())
        .isPresent()) {
      throw throwApplicationException(BRAND_NAME_ALREADY_EXISTS);
    }
    Brand brand = brandsRepository.findById(brandId)
        .orElseThrow(() -> throwApplicationException(NOT_FOUND));
    brand = setBrand(brand, request);
    brandsRepository.save(brand);
    return brandResponseConverter.convert(brand);
  }

  @Override
  public Optional<Brand> findById(Integer brandId) {
    return ObjectUtils.isEmpty(brandId) ? Optional.empty() : brandsRepository.findById(brandId);
  }

  @Override
  public List<Brand> findAllPopularBrands() {
    return brandsRepository.findAllByIsPopularIsTrue();
  }

  private Brand setBrand(Brand brand, BrandRequestDTO request) {
    return Brand.builder()
        .brandId(brand.getBrandId())
        .brandName(request.getBrandName())
        .brandNameJp(request.getBrandNameJp())
        .image(request.getBrandImage())
        .isVisible(request.getIsVisible())
        .isPopular(request.getIsPopular())
        .lastPopularAt(request.getIsPopular() ? DateUtil.timeNow() : null)
        .createdBy(brand.getCreatedBy())
        .createdOn(brand.getCreatedOn())
        .lastUpdatedBy(request.getUserId())
        .lastUpdatedOn(DateUtil.timeNow())
        .build();
  }
}
