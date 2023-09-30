package com.laplace.api.web.controller.user;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.dto.response.AddressDto;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.common.util.Messages;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.ADDRESS)
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class AddressController {

  private final WMCAddressService addressService;
  private final Messages messages;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public AddressController(WMCAddressService addressService, Messages messages,
      AuthenticationFacade authenticationFacade) {
    this.addressService = addressService;
    this.messages = messages;
    this.authenticationFacade = authenticationFacade;
  }

  @GetMapping
  public BaseResponse getAddresses() {
    Integer userId = authenticationFacade.getUserId();
    return BaseResponse.create(addressService.getAddresses(userId));
  }

  @GetMapping("/{id}")
  public BaseResponse getAddressById(@PathVariable int id) {
    Integer userId = authenticationFacade.getUserId();
    return BaseResponse.create(addressService.getAddressById(id, userId));
  }

  @PostMapping
  public BaseResponse saveAddress(@RequestBody AddressDto addressDto) {
    Integer userId = authenticationFacade.getUserId();
    return BaseResponse.create(addressService.saveAddress(addressDto, userId), messages.getAddressSavedSuccessful());
  }

  @PutMapping("/{id}")
  public BaseResponse updateAddress(@RequestBody AddressDto addressDto, @PathVariable int id) {
    Integer userId = authenticationFacade.getUserId();
    return BaseResponse.create(addressService.updateAddress(addressDto, userId, id), messages.getAddressUpdatedSuccessful());
  }

  @DeleteMapping("/{id}")
  public BaseResponse deleteAddress(@PathVariable Integer id) {
    Integer userId = authenticationFacade.getUserId();
    addressService.deleteAddress(id, userId);
    return BaseResponse.create(ApplicationConstants.OK_MSG);
  }
}