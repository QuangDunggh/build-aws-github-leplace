package com.laplace.api.common.service;

import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.configuration.email.MailDataProvider;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.ResultCodeConstants;
import com.laplace.api.common.model.db.AdminUser;
import com.laplace.api.common.model.db.CMSUserProfileModel;
import com.laplace.api.common.repository.db.AdminUserRepository;
import com.laplace.api.common.repository.db.ProfileSettingsRepository;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.laplace.api.common.util.LaplaceResponseUtil.throwApplicationException;

@Service
@Configuration
@Slf4j
public class EmailNotificationService {

  private final MailDataProvider mailDataProvider;
  private final MailConfiguration mailConfig;
  private ProfileSettingsRepository profileSettingsRepository;
  private AdminUserRepository adminUserRepository;
  private EmailService emailService;
  @Value("${cms.front.end.notification}")
  private String frontEndNotificationUrl;

  @Autowired
  public EmailNotificationService(ProfileSettingsRepository profileSettingsRepository,
      AdminUserRepository adminUserRepository,
      EmailService emailService, MailDataProvider mailDataProvider,
      MailConfiguration mailConfig) {
    this.profileSettingsRepository = profileSettingsRepository;
    this.adminUserRepository = adminUserRepository;
    this.emailService = emailService;
    this.mailDataProvider = mailDataProvider;
    this.mailConfig = mailConfig;
  }

  /**
   * Send email notification for publish request
   *
   * @param mailType
   * @param fromUserId
   * @param fromEmail
   * @param toUserId
   * @param mailTemplatePrefix
   * @param nType
   * @param nId
   */
  public void sendEmailNotification(int mailType,
      Integer fromUserId,
      String fromEmail,
      Integer toUserId,
      String mailTemplatePrefix,
      Integer nType,
      Integer nId,
      boolean isAd) {
    CMSUserProfileModel toUser = profileSettingsRepository.findById(toUserId)
        .orElseThrow(() -> throwApplicationException(
            ResultCodeConstants.USER_NOT_EXISTS));
    if (!toUser.isNotificationEnable()) {
      return;
    }

    List<String> toEmails = toUser.getNotificationEmailModels().stream().filter(Objects::nonNull)
        .map(emailModel -> emailModel.getNotificationEmailPK().getEmailAddress())
        .collect(Collectors.toList());

    AdminUser fromUser = adminUserRepository.findById(fromUserId)
        .orElseThrow(() -> throwApplicationException(
            ResultCodeConstants.USER_NOT_EXISTS));
    String fromName = null != fromUser.getProfile() ? fromUser.getProfile().getName() : null;
    Map<String, String> model = new HashMap<>();
    model.put("clientName", fromName);
    model
        .put("link", frontEndNotificationUrl + "?isAd=" + isAd + "&nType=" + nType + "&nId=" + nId);
    String subject = mailDataProvider.getMailSubjectCommon(mailType, Languages.JAPANESE);
    try {
      String body = mailDataProvider.getMailBody(model, mailTemplatePrefix, Languages.JAPANESE);
      sendEmail(toEmails, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(), subject,
          body);
    } catch (IOException | TemplateException e) {
      log.error("Can't send mail.{}", e.getLocalizedMessage(), e);
    }
  }

  @Async
  void sendEmail(List<String> toEmails, String fromEmail, String fromName, String subject,
                 String body) {
    try {
      for (String toEmail : toEmails) {
        emailService.sendMail(toEmail, fromEmail, fromName, subject, body);
      }
    } catch (MessagingException | UnsupportedEncodingException ex) {
      throwApplicationException(ResultCodeConstants.EMAIL_NOT_FOUND);
    }
  }
}
