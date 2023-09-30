package com.laplace.api.web.service;

import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.dto.response.ItemPackageResponseDTO;

public interface WMCItemPackageService {

  ItemPackageResponseDTO createPackage(ItemPackageRequestDTO itemPackageRequestDTO);
}
