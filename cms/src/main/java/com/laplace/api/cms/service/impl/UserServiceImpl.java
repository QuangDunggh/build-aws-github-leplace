package com.laplace.api.cms.service.impl;

import com.laplace.api.cms.service.UserService;
import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.dto.response.AppUserBasicInfo;
import com.laplace.api.common.dto.response.AppUserProfileResponseDto;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.ItemService;
import com.laplace.api.common.service.TokenService;
import com.laplace.api.common.service.UserInfoService;
import com.laplace.api.common.util.DateUtil;
import com.laplace.api.common.util.PageableResponseDTO;
import com.laplace.api.security.helper.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.laplace.api.common.constants.enums.ResultCodeConstants.BLACKLISTED;
import static com.laplace.api.common.constants.enums.ResultCodeConstants.USER_NOT_EXISTS;
import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
public class UserServiceImpl implements UserService {

  private final AppUserService appUserService;
  private final UserInfoService userInfoService;
  private final AuthenticationFacade authenticationFacade;
  private final TokenService tokenService;
  private final ItemService itemService;

  @Autowired
  public UserServiceImpl(AppUserService appUserService, UserInfoService userInfoService,
      AuthenticationFacade authenticationFacade,
      TokenService tokenService, ItemService itemService) {
    this.appUserService = appUserService;
    this.userInfoService = userInfoService;
    this.authenticationFacade = authenticationFacade;
    this.tokenService = tokenService;
    this.itemService = itemService;
  }

  @Override
  public AppUserProfileResponseDto getAppUserProfile(Integer userId) {
    return userInfoService.getUserProfileDetails(userId);

  }

  @Override
  public PageableResponseDTO<AppUserBasicInfo> getAppUserList(String keyword,
      List<VerificationStatus> verificationStatus, Pageable page) {
    Pageable request = PageRequest.of(page.getPageNumber(), page.getPageSize(),
        page.getSortOr(Sort.by(Sort.Direction.DESC, "createdOn")));
    Page<AppUser> userByPage = appUserService
        .findUserByKeywordAndVerificationStatus(keyword, verificationStatus, request);
    List<AppUser> userList = userByPage.getContent();
    return PageableResponseDTO.create(userByPage.getTotalElements(), userByPage.getTotalPages(),
        userList.stream().map(AppUserBasicInfo::from)
            .collect(Collectors.toList()));
  }

  @Override
  public void changeUserStatus(Integer userId) {
    AppUser appUser = appUserService.findById(userId)
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));
    if (appUser.getUserStatus().equals(UserStatus.BLACK_LISTED)) {
      throw throwApplicationException(BLACKLISTED);
    }

    itemService.setBlacklistedBySellerId(userId);

    appUser.setUserStatus(UserStatus.BLACK_LISTED);
    appUser.setLastUpdatedBy(authenticationFacade.getUserId());
    appUser.setLastUpdatedOn(DateUtil.timeNow());
    appUserService.saveUser(appUser);

    List<Token> tokenList = tokenService.findByUserId(userId).stream().filter(token ->
        token.getAppType() == AppType.WEB_CLIENT).collect(Collectors.toList());
    tokenService.deleteAll(tokenList);
  }

  @Override
  public void changeVerificationStatus(Integer userId, VerificationStatus verificationStatus) {
    AppUser appUser = appUserService.findById(userId)
        .orElseThrow(() -> throwApplicationException(USER_NOT_EXISTS));

    appUser.setVerificationStatus(verificationStatus);
    appUser.setLastUpdatedBy(authenticationFacade.getUserId());
    appUser.setLastUpdatedOn(DateUtil.timeNow());
    appUserService.saveUser(appUser);
    List<Token> tokenList = tokenService.findByUserIdAndAppType(userId, AppType.WEB_CLIENT);
    tokenService.deleteAll(tokenList);
  }
}
