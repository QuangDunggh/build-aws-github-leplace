package com.laplace.api.common.service.impl;

import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.converter.ItemPackageConverter;
import com.laplace.api.common.dto.request.ItemPackageRequestDTO;
import com.laplace.api.common.dto.response.ItemPackageResponseDTO;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.repository.db.ItemPackageRepository;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.ItemPackageService;
import com.laplace.api.common.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
@Slf4j
public class ItemPackageServiceImpl implements ItemPackageService {

  private final ItemPackageRepository itemPackageRepository;
  private final ItemService itemService;
  private final AppUserService appUserService;
  private final ItemPackageConverter itemPackageConverter;

  @Autowired
  ItemPackageServiceImpl(
      ItemPackageRepository itemPackageRepository,
      ItemService itemService, AppUserService appUserService,
      ItemPackageConverter itemPackageConverter) {
    this.itemPackageRepository = itemPackageRepository;
    this.itemService = itemService;
    this.appUserService = appUserService;
    this.itemPackageConverter = itemPackageConverter;
  }

  @Transactional
  @Override
  public ItemPackageResponseDTO createPackage(ItemPackageRequestDTO itemPackageRequestDTO) {
    AppUser appUser = appUserService.findById(itemPackageRequestDTO.getUserId()).orElseThrow(
        () -> throwApplicationException(ResultCodeConstants.USER_NOT_EXISTS));

    if (appUser.isAccountWithdrawn() || appUser.getUserStatus().equals(UserStatus.BLACK_LISTED) ||
        !appUser.getVerificationStatus().equals(VerificationStatus.VERIFIED)) {
      throw throwApplicationException(ResultCodeConstants.UNAUTHORIZED_OPERATION);
    }
    itemPackageRequestDTO.setEmail(appUser.getEmail());
    List<Item> items = itemService.saveItems(itemPackageRequestDTO);
    appUserService.incrementItemSentToLaPlaceCount(itemPackageRequestDTO.getUserId(),
        itemPackageRequestDTO.getItems().size());
    itemPackageRepository.save(
        Objects.requireNonNull(itemPackageConverter.convert(itemPackageRequestDTO)));

    return ItemPackageResponseDTO.from(itemPackageRequestDTO, items);
  }
}
