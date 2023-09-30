package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.response.AddressDto;
import com.laplace.api.common.model.db.Address;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter implements Converter<Address, AddressDto> {

  @Override
  public AddressDto convert(Address address) {
    return AddressDto.from(address);
  }
}
