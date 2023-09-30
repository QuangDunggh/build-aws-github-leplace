package com.laplace.api.web.service;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.ADDRESS_NOT_FOUND;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.dto.response.AddressDto;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.service.AddressService;
import com.laplace.api.common.service.cache.ZipCodeCacheService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class WMCAddressService {

  private final AddressService addressService;
  private final ZipCodeCacheService zipCodeCacheService;

  @Autowired
  public WMCAddressService(AddressService addressService,
      ZipCodeCacheService zipCodeCacheService) {
    this.addressService = addressService;
    this.zipCodeCacheService = zipCodeCacheService;
  }

  public List<AddressDto> getAddresses(Integer userId) {
    List<Address> addresses = addressService.getAddresses(userId);

    return addresses.stream().map(address -> AddressDto
            .from(address, zipCodeCacheService.getDeliveryFee(address.getZip())))
        .collect(Collectors.toList());
  }

  public AddressDto getAddressById(Integer id, Integer userId) {
    Address address = addressService.getAddressById(id);
    if (!address.getUserId().equals(userId) || address.getDeleted()) {
      throw throwApplicationException(ADDRESS_NOT_FOUND);
    }
    return AddressDto.from(address, zipCodeCacheService.getDeliveryFee(address.getZip()));
  }

  public AddressDto saveAddress(AddressDto addressDto, Integer userId) {
    addressDto.setAsDefault(ObjectUtils.isEmpty(addressService.getAddresses(userId)));
    Address address = addressService.saveAddress(addressDto, userId);
    return AddressDto.from(address, zipCodeCacheService.getDeliveryFee(addressDto.getZip()));
  }

  public AddressDto updateAddress(AddressDto addressDto, Integer userId, Integer id) {
    Address address = addressService.updateAddress(addressDto, userId, id);
    return AddressDto.from(address, zipCodeCacheService.getDeliveryFee(addressDto.getZip()));
  }

  public void deleteAddress(Integer id, Integer userId) {
    addressService.deleteAddress(id, userId);
  }

  /**
   * Save app user default address
   *
   * @param appUserAddress
   * @return
   */
  Address saveUserDefaultAddress(Address appUserAddress) {
    return addressService.saveUserDefaultAddress(appUserAddress);
  }

  Optional<Address> getAppUserDefaultAddress(Integer userId) {
    return addressService.getAppUserDefaultAddress(userId);
  }
}
