package com.laplace.api.web.service.impl;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

import com.laplace.api.common.constants.enums.FollowerCriteria;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.dto.response.FollowerResponseDTO;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.Follow;
import com.laplace.api.common.model.db.SellerProfile;
import com.laplace.api.common.service.AppUserService;
import com.laplace.api.common.service.FollowService;
import com.laplace.api.common.service.SellerProfileService;
import com.laplace.api.web.service.FollowerService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerServiceImpl implements FollowerService {

  private final FollowService followService;
  private final AppUserService appUserService;
  private final SellerProfileService sellerProfileService;

  @Autowired
  public FollowerServiceImpl(FollowService followService,
      AppUserService appUserService,
      SellerProfileService sellerProfileService) {
    this.followService = followService;
    this.appUserService = appUserService;
    this.sellerProfileService = sellerProfileService;
  }

  @Override
  public FollowerResponseDTO getFollowers(Integer userId, FollowerCriteria criteria) {
    switch (criteria) {
      case COUNT:
        return getFollowerCount(userId);
      case FOLLOWED_BY:
        return getFollowedByUsers(userId);
      case FOLLOWING:
        return getFollowingUsers(userId);
      default:
        throw throwApplicationException(ResultCodeConstants.INVALID_ARGUMENT);
    }
  }

  private FollowerResponseDTO getFollowerCount(Integer userId) {
    List<Follow> follows = followService.findAllByUserIdOrFollowedSellerId(userId, userId);
    Long followedByCount = follows.stream()
        .filter(follow -> follow.getSellerId().equals(userId))
        .count();
    Long followingCount = follows.stream()
        .filter(follow -> follow.getUserId().equals(userId))
        .count();

    return FollowerResponseDTO.from(followedByCount, followingCount, Collections.emptyList(),
        Collections.emptySet());
  }

  private FollowerResponseDTO getFollowedByUsers(Integer userId) {
    List<Follow> follows = followService.findAllByUserIdOrFollowedSellerId(userId, userId);
    Set<Integer> followedByUserIds = follows.stream()
        .filter(follow -> follow.getSellerId().equals(userId))
        .map(Follow::getUserId)
        .collect(Collectors.toSet());
    Set<Integer> followingUserIds = follows.stream()
        .filter(follow -> follow.getUserId().equals(userId))
        .map(Follow::getSellerId)
        .collect(Collectors.toSet());

    return FollowerResponseDTO.from((long) followedByUserIds.size(), null,
        getUserProfilePair(followedByUserIds), followingUserIds);
  }

  private FollowerResponseDTO getFollowingUsers(Integer userId) {
    List<Follow> follows = followService.findAllByUserId(userId);
    Set<Integer> followingUserIds = follows.stream()
        .map(Follow::getSellerId)
        .collect(Collectors.toSet());

    return FollowerResponseDTO.from(null, (long) follows.size(),
        getUserProfilePair(followingUserIds), followingUserIds);
  }

  private List<Pair<AppUser, SellerProfile>> getUserProfilePair(Set<Integer> userIds) {
    Map<Integer, AppUser> appUsers = appUserService.findByIds(userIds).stream()
        .collect(Collectors.toMap(AppUser::getUserId, Function.identity()));
    Map<Integer, SellerProfile> sellerProfiles = sellerProfileService.findByIdIn(userIds).stream()
        .collect(Collectors.toMap(SellerProfile::getSellerId, Function.identity()));

    return userIds.stream()
        .map(key -> Pair.of(appUsers.get(key), sellerProfiles.get(key)))
        .collect(Collectors.toList());
  }
}
