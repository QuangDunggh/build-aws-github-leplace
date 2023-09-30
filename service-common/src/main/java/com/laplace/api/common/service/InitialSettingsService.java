package com.laplace.api.common.service;

import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.model.db.GiftWrappingOption;
import com.laplace.api.common.model.db.InitialSettings;
import java.util.List;
import java.util.Optional;

public interface InitialSettingsService {

  Optional<InitialSettings> getInitialSettings();

  Optional<InitialSettings> getInitialSettingsFromDB();

  void saveInitialSettings(InitialSettings initialSettings);

  List<GiftWrappingOption> getAllGiftWrappingOptions();

  GiftWrappingDTO getGiftWrappingDetails(GiftWrappingType giftWrappingType);
}
