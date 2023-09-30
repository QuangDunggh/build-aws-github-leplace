package com.laplace.api.web.service.impl;

import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.web.service.WMCInitialSettingsService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WMCInitialSettingsServiceImpl implements WMCInitialSettingsService {

  private final InitialSettingsService initialSettingsService;

  @Autowired
  WMCInitialSettingsServiceImpl(
      InitialSettingsService initialSettingsService) {
    this.initialSettingsService = initialSettingsService;
  }

  @Override
  public InitialSettingsDTO getInitialSettings() {
    return InitialSettingsDTO
        .from(initialSettingsService.getInitialSettings().orElse(null));
  }

  @Override
  public List<GiftWrappingDTO> getAllGiftOptions() {
    return initialSettingsService.getAllGiftWrappingOptions().stream()
        .map(GiftWrappingDTO::from)
        .collect(Collectors.toList());
  }
}
