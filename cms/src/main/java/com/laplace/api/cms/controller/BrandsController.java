package com.laplace.api.cms.controller;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.service.CMSBrandsService;
import com.laplace.api.common.constants.TextLength;
import com.laplace.api.common.dto.request.BrandRequestDTO;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.helper.AuthenticationFacade;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.BRANDS)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BrandsController {

  private final CMSBrandsService cmsBrandsService;
  private final Messages messages;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public BrandsController(CMSBrandsService cmsBrandsService, Messages messages,
      AuthenticationFacade authenticationFacade) {
    this.cmsBrandsService = cmsBrandsService;
    this.messages = messages;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping()
  public BaseResponse getAllBrands() {
    return BaseResponse.create(cmsBrandsService.getAllBrands());
  }

  @GetMapping(APIEndPoints.POPULAR)
  public BaseResponse getAllPopularBrands(
      @PageableDefault(size = TextLength.POPULAR_BRAND_DEFAULT_SIZE,
          sort = {"lastPopularAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
    return BaseResponse.create(cmsBrandsService.getPopularBrands(pageable));
  }

  @GetMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse getBrandsById(@PathVariable int id) {
    return BaseResponse.create(cmsBrandsService.getBrandsById(id));
  }

  @PostMapping
  public BaseResponse createBrand(@Valid @RequestBody BrandRequestDTO brandRequestDTO) {
    brandRequestDTO.setUserId(authenticationFacade.getUserId());
    return BaseResponse
        .create(cmsBrandsService.addBrand(brandRequestDTO), messages.getBrandCreated());
  }

  @PutMapping(APIEndPoints.PATH_VARIABLE_ID)
  public BaseResponse updateBrand(@Valid @RequestBody BrandRequestDTO brandRequestDTO,
      @PathVariable int id) {
    brandRequestDTO.setUserId(authenticationFacade.getUserId());
    return BaseResponse
        .create(cmsBrandsService.updateBrand(id, brandRequestDTO), messages.getBrandUpdated());
  }
}
