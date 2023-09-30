package com.laplace.api.common.service;

import com.laplace.api.common.dto.response.AddressDto;
import com.laplace.api.common.model.db.Address;
import java.util.List;
import java.util.Optional;

public interface AddressService {

  List<Address> getAddresses(Integer userId) ;

  Address getAddressById(Integer id);

  Address saveAddress(AddressDto addressDto, Integer userId);

  Address updateAddress(AddressDto addressDto, Integer userId, Integer id);

  void deleteAddress(Integer id, Integer userId);

  Address saveUserDefaultAddress(Address appUserAddress);

  Optional<Address> getAppUserDefaultAddress(Integer userId);
}
