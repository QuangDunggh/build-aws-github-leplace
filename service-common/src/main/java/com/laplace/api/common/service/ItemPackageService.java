package com.laplace.api.common.service;

import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.dto.response.ItemPackageResponseDTO;

public interface ItemPackageService {

  ItemPackageResponseDTO createPackage(ItemPackageRequestDTO itemPackageRequestDTO);
}
