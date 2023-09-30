package com.laplace.api.web.controller;

import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCInitialSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.INITIAL_SETTINGS)
public class WMCInitialSettingsController {

  private final WMCInitialSettingsService wmcInitialSettingsService;

  @Autowired
  public WMCInitialSettingsController(
      WMCInitialSettingsService wmcInitialSettingsService) {
    this.wmcInitialSettingsService = wmcInitialSettingsService;
  }

  @GetMapping()
  public BaseResponse getInitialSettings() {
    return BaseResponse.create(wmcInitialSettingsService.getInitialSettings());
  }

  @GetMapping(APIEndPoints.WRAPPING_OPTIONS)
  public BaseResponse getGiftOptions() {
    return BaseResponse.create(wmcInitialSettingsService.getAllGiftOptions());
  }
}
