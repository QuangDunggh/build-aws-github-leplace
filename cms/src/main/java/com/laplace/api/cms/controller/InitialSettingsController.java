package com.laplace.api.cms.controller;

import com.laplace.api.cms.constants.APIEndPoints;
import com.laplace.api.cms.service.CMSInitialSettingsService;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.util.BaseResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequestMapping(APIEndPoints.INITIAL_SETTINGS)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class InitialSettingsController {

  private final CMSInitialSettingsService cmsInitialSettingsService;

  @Autowired
  InitialSettingsController(
      CMSInitialSettingsService cmsInitialSettingsService) {
    this.cmsInitialSettingsService = cmsInitialSettingsService;
  }

  @GetMapping()
  public BaseResponse getInitialSettings() {
    return BaseResponse.create(cmsInitialSettingsService.getInitialSettings());
  }

  @PutMapping()
  public BaseResponse setInitialSettings(
      @Valid @RequestBody InitialSettingsDTO initialSettingsDTO) {
    cmsInitialSettingsService.saveInitialSettings(initialSettingsDTO);
    return BaseResponse.create();
  }
}
