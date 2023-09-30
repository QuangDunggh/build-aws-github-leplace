package com.laplace.api.web.service;

import com.laplace.api.common.dto.request.PriceNegotiationRequestDto;

public interface PriceNegotiationService {
    void submitNegotiationPrice(String itemId, PriceNegotiationRequestDto priceNegotiationRequestDto);
}
