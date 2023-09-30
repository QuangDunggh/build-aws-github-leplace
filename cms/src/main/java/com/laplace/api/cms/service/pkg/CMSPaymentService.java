package com.laplace.api.cms.service.pkg;

import com.laplace.api.cms.core.dto.TransactionResponseDTO;
import org.springframework.data.domain.Pageable;

public interface CMSPaymentService {

  TransactionResponseDTO getAllPayments(Integer userId, Integer month, Integer year,
      Pageable pageable);
}
