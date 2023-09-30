package com.laplace.api.common.service.cache;

import static com.laplace.api.common.constants.ApplicationConstants.JP_ZIP_CODE_LENGTH;
import static com.laplace.api.common.constants.ApplicationConstants.JP_ZIP_CODE_PREFIX_LENGTH;
import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;
import static com.laplace.api.common.constants.ZipCodeConstants.HOKKAIDO_ZIP_CODE_PREFIX;
import static com.laplace.api.common.constants.ZipCodeConstants.OKINAWA_ZIP_CODE_PREFIX;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.Region;
import com.laplace.api.common.dto.DeliveryFeeDto;
import com.laplace.api.common.model.db.InitialSettings;
import com.laplace.api.common.repository.redis.ZipcodeCacheRepository;
import com.laplace.api.common.service.InitialSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class ZipCodeCacheService {

  private final ZipcodeCacheRepository zipcodeCacheRepository;
  private final InitialSettingsService initialSettingsService;

  @Autowired
  public ZipCodeCacheService(ZipcodeCacheRepository zipcodeCacheRepository,
      InitialSettingsService initialSettingsService) {
    this.zipcodeCacheRepository = zipcodeCacheRepository;
    this.initialSettingsService = initialSettingsService;
  }

  public DeliveryFeeDto getDeliveryFee(String zipCode) {
    InitialSettings settings = initialSettingsService.getInitialSettings().orElse(null);
    if (ObjectUtils.isEmpty(settings)) {
      return DeliveryFeeDto.from(ApplicationConstants.VALUE_ZERO, ApplicationConstants.VALUE_ZERO);
    }
    if (zipcodeCacheRepository.isZipCodeExist(Region.ISLAND, zipCode)) {
      return DeliveryFeeDto.from(settings.getDeliveryFeeIsland(),
          settings.getCancelDeliveryFeeIsland());
    }
    if (zipcodeCacheRepository.isZipCodeExist(Region.OKINAWA, zipCode)) {
      return DeliveryFeeDto.from(settings.getDeliveryFeeOkinawa(),
          settings.getCancelDeliveryFeeOkinawa());
    }
    if (zipcodeCacheRepository.isZipCodeExist(Region.HOKKAIDO, zipCode)) {
      return DeliveryFeeDto.from(settings.getDeliveryFeeHokkaido(),
          settings.getCancelDeliveryFeeHokkaido());
    }
    if (OKINAWA_ZIP_CODE_PREFIX.contains(zipCode.substring(VALUE_ZERO, JP_ZIP_CODE_PREFIX_LENGTH))
        && zipCode.length() == JP_ZIP_CODE_LENGTH) {
      return DeliveryFeeDto.from(settings.getDeliveryFeeOkinawa(),
          settings.getCancelDeliveryFeeOkinawa());
    }
    if (HOKKAIDO_ZIP_CODE_PREFIX.contains(zipCode.substring(VALUE_ZERO, JP_ZIP_CODE_PREFIX_LENGTH))
        && zipCode.length() == JP_ZIP_CODE_LENGTH) {
      return DeliveryFeeDto.from(settings.getDeliveryFeeHokkaido(),
          settings.getCancelDeliveryFeeHokkaido());
    }
    return DeliveryFeeDto.from(settings.getDeliveryFee(), settings.getCancelDeliveryFee());
  }
}
