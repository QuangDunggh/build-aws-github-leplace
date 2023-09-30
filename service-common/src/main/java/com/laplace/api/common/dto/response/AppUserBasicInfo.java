package com.laplace.api.common.dto.response;

import static com.laplace.api.common.constants.ApplicationConstants.ONE;
import static com.laplace.api.common.constants.ApplicationConstants.PLUS_REGEX;

import com.laplace.api.common.constants.enums.UserStatus;
import com.laplace.api.common.constants.enums.VerificationStatus;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.model.db.SellerProfile;
import java.util.Map;
import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserBasicInfo {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private String userIcon;
    private Boolean isBlackList;
    private Long numberOfItemsSentToLaplace;
    private Long numberOfItemsOnDisplay;
    private Long numberOfItemsSold;
    private Long registeredDate;
    private VerificationStatus idVerificationStatus;
    private Boolean facebookEnable;
    private Boolean twitterEnable;
    private Long unreadCount;
    private Boolean accountWithdrawn;

    public static AppUserBasicInfo from(AppUser user) {
        AppUserProfile profile = user.getOrEmptyProfile();
        return AppUserBasicInfo.builder()
                .email((user.isAccountWithdrawn()) ?
                    trimWithdrawnEmail(user.getEmail()) : user.getEmail())
                .userId(user.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .userName(profile.getUserName())
                .userIcon(profile.getProfileImage())
                .idVerificationStatus(user.getVerificationStatus())
                .isBlackList(user.getUserStatus() == UserStatus.BLACK_LISTED)
                .numberOfItemsOnDisplay(user.getItemsOnDisplay())
                .numberOfItemsSentToLaplace(user.getItemsSentToLaplace())
                .numberOfItemsSold(user.getItemsSold())
                .registeredDate(user.getCreatedOn().toEpochSecond())
                .facebookEnable(user.isFacebookEnable())
                .twitterEnable(user.isTwitterEnable())
                .unreadCount(user.getUnreadCount())
                .accountWithdrawn(user.isAccountWithdrawn())
                .build();
    }

    public static AppUserBasicInfo from(AppUser user,
        Map<Integer, SellerProfile> sellerProfileMap) {
        SellerProfile sellerProfile = sellerProfileMap.get(user.getUserId());
        AppUserBasicInfo basicInfo = from(user);
        if (Objects.isNull(sellerProfile)) {
            return basicInfo;
        }
        basicInfo.setUserIcon(sellerProfile.getProfileImage());
        return basicInfo;
    }

    private static String trimWithdrawnEmail(String email) {
        String[] splitEmail = email.split(PLUS_REGEX, 2);
        return splitEmail[Math.min((splitEmail.length - ONE), ONE)];
    }
}
