package com.laplace.api.common.service.impl;

import static com.laplace.api.common.constants.ApplicationConstants.JP_COUNTRY_CODE_LENGTH;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.USER_NOT_EXISTS;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.dto.response.AppUserAddressBasicInfo;
import com.laplace.api.common.dto.response.AppUserBasicInfo;
import com.laplace.api.common.dto.response.AppUserDetailsInfo;
import com.laplace.api.common.dto.response.AppUserProfileResponseDto;
import com.laplace.api.common.dto.response.BankAccountInfo;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.pay.jp.CustomerService;
import com.laplace.api.common.repository.db.AppUserProfileRepository;
import com.laplace.api.common.service.AddressService;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.UserInfoService;
import com.laplace.api.common.util.DateUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import java.util.Comparator;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

  private final AppUserProfileRepository appUserProfileRepository;
  private final AppUserService appUserService;
  private final AddressService addressService;
  private final CustomerService customerService;

  @Autowired
  public UserInfoServiceImpl(AppUserProfileRepository appUserProfileRepository,
      AppUserService appUserService,
      AddressService addressService, CustomerService customerService) {
    this.appUserProfileRepository = appUserProfileRepository;
    this.appUserService = appUserService;
    this.addressService = addressService;
    this.customerService = customerService;
  }

  @Override
  public Optional<AppUserProfile> findById(Integer userId) {
    return appUserProfileRepository.findById(userId);
  }

  @Override
  public AppUserProfileResponseDto getUserProfileDetails(Integer userId) {
    AppUser appUser = appUserService.findById(userId)
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    AppUserProfile appUserProfile = appUser.getOrEmptyProfile();

    Address defaultAddress = addressService.getAddresses(userId).stream()
        .max(Comparator.comparing(address -> !ObjectUtils.isEmpty(address.getAsDefault())
            && address.getAsDefault().equals(true)))
        .orElse(null);

    BankAccount bankAccount = null;

    try {
      bankAccount = customerService.getBankAccountInfo(userId);
    } catch (StripeException err) {
      log.info(err.getLocalizedMessage());
    }

    return AppUserProfileResponseDto.builder()
        .basicInfo(AppUserBasicInfo.from(appUser))
        .detailsInfo(AppUserDetailsInfo.builder()
            .dateOfBirth(DateUtil.toEpochMilli(appUserProfile.getBirthDate()))
            .day(appUserProfile.getBirthDate().getDayOfMonth())
            .month(appUserProfile.getBirthDate().getMonthValue())
            .year(appUserProfile.getBirthDate().getYear())
            .phoneNumber(appUserProfile.getPhoneNumber().substring(JP_COUNTRY_CODE_LENGTH))
            .firstNameKana(appUserProfile.getFirstNameKana())
            .lastNameKana(appUserProfile.getLastNameKana())
            .userName(appUserProfile.getUserName())
            .address(AppUserAddressBasicInfo.make(defaultAddress))
            .build())
        .bankAccountInfo(BankAccountInfo.make(bankAccount))
        .build();
  }
}
