package com.laplace.api.common.dto.response;

import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.SellerProfile;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.ObjectUtils;

@Data
@Builder
public class FollowerResponseDTO {

  Long followedByCount;
  Long followingCount;
  List<FollowerBasicDetails> followerInfo;

  @Data
  @Builder
  public static class FollowerBasicDetails {

    Integer userId;
    String nickName;
    String profileImage;
    Boolean isFollowing;
    VerificationStatus verificationStatus;
  }

  public static FollowerResponseDTO from(Long followedByCount, Long followingCount,
      List<Pair<AppUser, SellerProfile>> users, Set<Integer> followerIds) {
    return FollowerResponseDTO.builder()
        .followedByCount(followedByCount)
        .followingCount(followingCount)
        .followerInfo(users.stream()
            .map(user -> from(user.getLeft(), user.getRight(),
                followerIds.contains(user.getLeft().getUserId())))
            .sorted(Comparator.comparing(
                followerBasicDetails -> followerBasicDetails.getNickName().toLowerCase()))
            .collect(Collectors.toList()))
        .build();
  }

  private static FollowerBasicDetails from(AppUser user, SellerProfile sellerProfile,
      Boolean isFollowing) {
    return FollowerBasicDetails.builder()
        .userId(user.getUserId())
        .nickName(user.getOrEmptyProfile().getUserName())
        .profileImage(ObjectUtils.isEmpty(sellerProfile) ? null : sellerProfile.getProfileImage())
        .isFollowing(isFollowing)
        .verificationStatus(user.getVerificationStatus())
        .build();
  }
}
