package com.laplace.api.common.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodResponseDTO {
    private String paymentMethod;
    private String cardType;
    private String lastFourDigit;
    private String cardHolderName;
    private String expiration;
}
