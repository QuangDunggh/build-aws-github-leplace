package com.laplace.api.common.service.impl;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.ADDRESS_NOT_FOUND;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.response.AddressDto;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.repository.db.AddressRepository;
import com.laplace.api.common.repository.db.AppUserRepository;
import com.laplace.api.common.service.AddressService;
import com.laplace.api.common.util.DateUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class AddressServiceImpl implements AddressService {

  private final AppUserRepository appUserRepository;
  private final AddressRepository addressRepository;

  @Autowired
  public AddressServiceImpl(AppUserRepository appUserRepository,
      AddressRepository addressRepository) {
    this.appUserRepository = appUserRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  public List<Address> getAddresses(Integer userId) {
    return addressRepository.findByUserIdAndDeleted(userId, false);
  }

  @Override
  public Address getAddressById(Integer id) {
    return addressRepository.findById(id)
        .orElseThrow(() -> throwApplicationException(ADDRESS_NOT_FOUND));
  }

  @Override
  public Address saveAddress(AddressDto addressDto, Integer userId) {
    Address address = AddressDto.copyProperties(addressDto, new Address());
    address.setUserId(userId);
    address.setDeleted(false);
    address.setCreatedOn(DateUtil.timeNow());
    address.setCreatedBy(userId);
    return addressRepository.save(address);
  }

  @Override
  public Address updateAddress(AddressDto addressDto, Integer userId, Integer id) {
    Address address = addressRepository.findByIdAndUserIdAndDeleted(id, userId, false)
        .orElseThrow(() -> throwApplicationException(ADDRESS_NOT_FOUND));

    AddressDto.copyProperties(addressDto, address);
    address.setId(id);
    address.setUserId(userId);
    address.setLastUpdatedOn(DateUtil.timeNow());
    address.setLastUpdatedBy(userId);
    return addressRepository.save(address);
  }

  @Override
  public void deleteAddress(Integer id, Integer userId) {
    if (getAddresses(userId).size() <= ApplicationConstants.ONE) {
      throw throwApplicationException(ResultCodeConstants.REQUIRED_ONE_ADDRESS);
    }

    Address address = addressRepository.findByIdAndUserIdAndDeleted(id, userId, false)
        .orElseThrow(() -> throwApplicationException(ADDRESS_NOT_FOUND));
    address.setDeleted(true);
    addressRepository.save(address);

    Address defaultAddress = getAddresses(userId).stream()
        .max(Comparator.comparing(
            a -> !ObjectUtils.isEmpty(a.getAsDefault()) && a.getAsDefault().equals(true)))
        .orElse(null);
    if (!ObjectUtils.isEmpty(defaultAddress)) {
      defaultAddress.setAsDefault(true);
      addressRepository.save(defaultAddress);
    }
  }


  /**
   * Save app user default address
   *
   * @param address
   * @return
   */
  @Override
  public Address saveUserDefaultAddress(Address address) {
    return addressRepository.save(address);
  }

  @Override
  public Optional<Address> getAppUserDefaultAddress(Integer userId) {
    return addressRepository.findByUserIdAndAsDefault(userId, true);
  }
}

