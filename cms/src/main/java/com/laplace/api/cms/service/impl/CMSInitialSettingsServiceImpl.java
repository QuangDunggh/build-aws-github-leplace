package com.laplace.api.cms.service.impl;

import com.laplace.api.cms.service.CMSInitialSettingsService;
import com.laplace.api.common.constants.enums.RemindPeriod;
import com.laplace.api.common.converter.InitialSettingsConverter;
import com.laplace.api.common.dto.InitialSettingsDTO;
import com.laplace.api.common.service.InitialSettingsService;
import com.laplace.api.security.helper.AuthenticationFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CMSInitialSettingsServiceImpl implements CMSInitialSettingsService {

  private final InitialSettingsService initialSettingsService;
  private final AuthenticationFacade authenticationFacade;
  private final InitialSettingsConverter initialSettingsConverter;

  @Autowired
  CMSInitialSettingsServiceImpl(
      InitialSettingsService initialSettingsService,
      AuthenticationFacade authenticationFacade,
      InitialSettingsConverter initialSettingsConverter) {
    this.initialSettingsService = initialSettingsService;
    this.authenticationFacade = authenticationFacade;
    this.initialSettingsConverter = initialSettingsConverter;
  }

  @Override
  public InitialSettingsDTO getInitialSettings() {
    return InitialSettingsDTO
        .from(initialSettingsService.getInitialSettingsFromDB().orElse(null));
  }

  @Override
  public void saveInitialSettings(InitialSettingsDTO initialSettingsDTO) {
    Integer userId = authenticationFacade.getUserId();
    initialSettingsDTO.setRemindPeriod(RemindPeriod.ONE_WEEK);
    initialSettingsDTO.setUpdatedBy(userId);
    initialSettingsDTO.getGiftWrappingOptions()
        .forEach(giftWrappingDTO -> giftWrappingDTO.setUpdatedBy(userId));
    initialSettingsService
        .saveInitialSettings(initialSettingsConverter.convert(initialSettingsDTO));
  }
}
