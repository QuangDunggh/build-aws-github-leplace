package com.laplace.api.web.service.impl;

import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.dto.response.ItemPackageResponseDTO;
import com.laplace.api.common.service.ItemPackageService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.security.helper.AuthenticationFacade;
import com.laplace.api.web.service.WMCItemPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WMCItemPackageServiceImpl implements WMCItemPackageService {

  private final ItemPackageService itemPackageService;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  private WMCItemPackageServiceImpl(ItemPackageService itemPackageService,
      AuthenticationFacade authenticationFacade) {
    this.itemPackageService = itemPackageService;
    this.authenticationFacade = authenticationFacade;
  }

  public ItemPackageResponseDTO createPackage(ItemPackageRequestDTO itemPackageRequestDTO) {
    Integer userId = authenticationFacade.getUserId();
    itemPackageRequestDTO.setUserId(userId);
    itemPackageRequestDTO.setPackageId(DateUtil.getUniqueTimeBasedUUID());

    return itemPackageService.createPackage(itemPackageRequestDTO);
  }
}
