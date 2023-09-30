package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.response.BrandResponseDTO;
import com.laplace.api.common.model.db.Brand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BrandResponseConverter implements Converter<Brand, BrandResponseDTO> {

  @Override
  public BrandResponseDTO convert(Brand source) {
    return BrandResponseDTO.builder()
        .brandId(source.getBrandId())
        .brandName(source.getBrandName())
        .brandNameJp(source.getBrandNameJp())
        .brandImage(source.getImage())
        .isPopular(source.getIsPopular())
        .isVisible(source.getIsVisible())
        .lastPopularAt(source.getLastPopularAt())
        .build();
  }
}
