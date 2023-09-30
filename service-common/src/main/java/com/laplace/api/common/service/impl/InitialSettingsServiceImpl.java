package com.laplace.api.common.service.impl;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.GiftWrappingType;
import com.laplace.api.common.dto.GiftWrappingDTO;
import com.laplace.api.common.model.db.GiftWrappingOption;
import com.laplace.api.common.model.db.InitialSettings;
import com.laplace.api.common.repository.db.GiftWrappingOptionRepository;
import com.laplace.api.common.repository.db.InitialSettingsRepository;
import com.laplace.api.common.service.InitialSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.laplace.api.common.constants.ApplicationConstants.ONE_STRING;

@Service
@Slf4j
public class InitialSettingsServiceImpl implements InitialSettingsService {

  private static final String INITIAL_SETTINGS_CACHE = "INITIAL_SETTINGS_CACHE";
  private final InitialSettingsRepository initialSettingsRepository;
  private final GiftWrappingOptionRepository giftWrappingOptionRepository;

  @Autowired
  InitialSettingsServiceImpl(
      InitialSettingsRepository initialSettingsRepository,
      GiftWrappingOptionRepository giftWrappingOptionRepository) {
    this.initialSettingsRepository = initialSettingsRepository;
    this.giftWrappingOptionRepository = giftWrappingOptionRepository;
  }

  @Cacheable(value = INITIAL_SETTINGS_CACHE, key = ONE_STRING)
  @Override
  public Optional<InitialSettings> getInitialSettings() {
    return initialSettingsRepository.findById(ApplicationConstants.ONE);
  }

  @Override
  public Optional<InitialSettings> getInitialSettingsFromDB() {
    return initialSettingsRepository.findById(ApplicationConstants.ONE);
  }

  @CacheEvict(value = INITIAL_SETTINGS_CACHE, key = ONE_STRING)
  @Override
  public void saveInitialSettings(InitialSettings initialSettings) {
    initialSettingsRepository.save(Objects.requireNonNull(initialSettings));
  }

  @Override
  public List<GiftWrappingOption> getAllGiftWrappingOptions() {
    return Streamable.of(giftWrappingOptionRepository.findAll()).toList();
  }

  @Override
  public GiftWrappingDTO getGiftWrappingDetails(GiftWrappingType giftWrappingType) {
    return GiftWrappingDTO.from(
        giftWrappingOptionRepository.findByGiftWrappingType(giftWrappingType).orElse(null));
  }
}
