package com.laplace.api.cms.service;

import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Role;
import com.laplace.api.common.model.db.VerificationToken;
import org.springframework.scheduling.annotation.Async;

public interface CMSEmailService {

  @Async("taskExecutor")
  void sendResetPasswordMail(AdminUser user, VerificationToken verificationToken, String language);

  @Async("taskExecutor")
  void sendInvitationMail(AdminUser invitedUser, VerificationToken token, Role role,
      String language);

  @Async("taskExecutor")
  void sendFakeJudgementMail(Item item, AppUser user);

  @Async("taskExecutor")
  void sendOnSaleMail(Item item, AppUser user);

  @Async("taskExecutor")
  void sendReadyToDeliveredMail(Order order, AppUser user);

  @Async("taskExecutor")
  void sendItemReturnCompletedMail(Order order, AppUser user);
}
