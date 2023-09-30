package com.laplace.api.web.controller;

import com.laplace.api.common.constants.TextLength;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCBrandsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.BRANDS)
public class BrandController {

  private final WMCBrandsService wmcBrandsService;

  @Autowired
  public BrandController(WMCBrandsService wmcBrandsService) {
    this.wmcBrandsService = wmcBrandsService;
  }

  @GetMapping()
  public BaseResponse getAllBrands() {
    return BaseResponse.create(wmcBrandsService.getAllBrands());
  }

  @GetMapping(APIEndPoints.POPULAR)
  public BaseResponse getAllPopularBrands(
      @PageableDefault(size = TextLength.POPULAR_BRAND_DEFAULT_SIZE,
          sort = {"lastPopularAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
    return BaseResponse.create(wmcBrandsService.getPopularBrands(pageable));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse getBrandsById(@PathVariable int id) {
    return BaseResponse.create(wmcBrandsService.getBrandsById(id));
  }
}
