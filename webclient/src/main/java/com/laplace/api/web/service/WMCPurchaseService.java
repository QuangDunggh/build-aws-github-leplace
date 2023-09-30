package com.laplace.api.web.service;

import com.laplace.api.common.constants.enums.PurchaseStatus;
import com.laplace.api.common.dto.response.PurchaseResponseDTO;
import com.laplace.api.common.dto.response.PurchasedItemResponseDTO;
import com.laplace.api.common.util.PageableResponseDTO;
import org.springframework.data.domain.Pageable;

public interface WMCPurchaseService {

  PageableResponseDTO<PurchasedItemResponseDTO> findPurchasedItems(PurchaseStatus purchaseStatus,
      Pageable pageable);

  PurchaseResponseDTO findPurchaseDetails(String orderId);
}
