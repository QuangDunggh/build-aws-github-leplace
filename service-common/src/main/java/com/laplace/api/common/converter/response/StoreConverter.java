package com.laplace.api.common.converter.response;

import com.laplace.api.common.dto.AddressDto;
import com.laplace.api.common.model.db.Store;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StoreConverter implements Converter<Store, AddressDto> {

  @Override
  public AddressDto convert(Store store) {
    return AddressDto.builder()
        .id(String.valueOf(store.getId()))
        .clientId(store.getClientId())
        .name(store.getName())
        .location(store.getLocation())
        .city(store.getCity())
        .zip(store.getZip())
        .stateId(store.getStateId())
        .line1(store.getLine1())
        .line2(store.getLine2())
        .storageId(store.getStorageId())
        .createdAt(store.getCreatedAt())
        .updatedAt(store.getUpdatedAt())
        .build();
  }
}