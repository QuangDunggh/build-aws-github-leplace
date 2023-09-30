package com.laplace.api.web.controller;

import com.laplace.api.common.dto.request.PriceNegotiationRequestDto;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.PriceNegotiationService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SELLER')")
public class PriceNegotiationController {

  private final PriceNegotiationService priceNegotiationService;

  @Autowired
  public PriceNegotiationController(PriceNegotiationService priceNegotiationService) {
    this.priceNegotiationService = priceNegotiationService;
  }

  @PostMapping(APIEndPoints.NEGOTIATE)
  public BaseResponse submitNegotiationPrice(@PathVariable("id") String itemId,
      @Valid @RequestBody PriceNegotiationRequestDto priceNegotiationRequestDto) {
    priceNegotiationService.submitNegotiationPrice(itemId, priceNegotiationRequestDto);
    return BaseResponse.create();
  }
}
