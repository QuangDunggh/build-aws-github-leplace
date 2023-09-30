package com.laplace.api.cms.service;

import com.laplace.api.common.dto.InitialSettingsDTO;

public interface CMSInitialSettingsService {

  InitialSettingsDTO getInitialSettings();

  void saveInitialSettings(InitialSettingsDTO initialSettingsDTO);
}
