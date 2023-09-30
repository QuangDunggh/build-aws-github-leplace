package com.laplace.api.web.service;

import com.laplace.api.common.dto.response.DepositResponseDTO;
import com.laplace.api.common.dto.response.ExpendituresResponseDTO;
import org.springframework.data.domain.Pageable;

public interface WMCPaymentHistoryService {

  DepositResponseDTO findDeposits(Integer year, Integer month,
      Pageable pageable);

  ExpendituresResponseDTO findExpenditures(Integer year, Integer month, Pageable pageable);
}
