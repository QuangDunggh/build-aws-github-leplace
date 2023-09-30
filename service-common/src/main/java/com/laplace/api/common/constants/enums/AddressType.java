package com.laplace.api.common.constants.enums;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.INVALID_ARGUMENT;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import java.util.function.Supplier;
import java.util.stream.Stream;

public enum AddressType {
  STORE(1),
  ADDRESS(2),
  HOTEL(3);

  private Integer value;

  AddressType(Integer value) {
    this.value = value;
  }

  private static Supplier<Stream<AddressType>>
  addressTypeSupplier() {
    return () -> Stream.of(AddressType.values());
  }

  public static AddressType getAddressTypeFromVal(int val) {
    return addressTypeSupplier().get()
        .filter(addressType -> addressType.getValue() == val)
        .findFirst().orElseThrow(() -> throwApplicationException(INVALID_ARGUMENT));
  }

  public Integer getValue() {
    return value;
  }
}
