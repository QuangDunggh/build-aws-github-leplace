package com.laplace.api.web.service;

import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.dto.InitialSettingsDTO;
import java.util.List;

public interface WMCInitialSettingsService {

  InitialSettingsDTO getInitialSettings();

  List<GiftWrappingDTO> getAllGiftOptions();
}
