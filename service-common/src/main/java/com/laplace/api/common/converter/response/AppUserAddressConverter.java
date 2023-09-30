package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.AddressDto;
import com.laplace.api.common.model.db.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AppUserAddressConverter implements Converter<Address, AddressDto> {

  @Override
  public AddressDto convert(Address address) {

    return AddressDto.builder()
        .id(String.valueOf(address.getId()))
        .userId(address.getUserId())
        .zip(address.getZip())
        .build();
  }
}