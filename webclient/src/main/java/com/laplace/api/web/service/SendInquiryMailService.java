package com.laplace.api.web.service;

import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.configuration.email.MailDataProvider;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.MailTemplateName;
import com.laplace.api.common.service.EmailService;
import com.laplace.api.web.core.bean.InquiryBean;
import com.laplace.api.web.core.enums.InquiryType;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SendInquiryMailService {

  private static final String SENT_MAIL_FAILED_LOG_TEMPLATE = "Can't send mail.{}";
  private final EmailService emailService;
  private final MailDataProvider mailDataProvider;
  private final MailConfiguration mailConfig;

  @Autowired
  public SendInquiryMailService(EmailService emailService,
      MailDataProvider mailDataProvider,
      MailConfiguration mailConfig) {
    this.emailService = emailService;
    this.mailDataProvider = mailDataProvider;
    this.mailConfig = mailConfig;
  }


  public void send(String sender, InquiryType inquiryType, String content, String lang) {
    Map<String, String> model = new HashMap<>(2);
    model.put(ApplicationConstants.InquiryMailTemplateFields.NAME, sender);
    model.put(ApplicationConstants.InquiryMailTemplateFields.CONTENT, content);

    try {
      model
          .put(ApplicationConstants.InquiryMailTemplateFields.SUPER_ADMIN,
              ApplicationConstants.StringUtils.EMPTY_STRING);
      String subject = String.format(mailDataProvider
              .getMailSubjectCommon(ApplicationConstants.MailType.WMC_POST_INQUIRY_ADMIN, lang),
          inquiryType.getValue());
      String body = mailDataProvider
          .getMailBody(model, MailTemplateName.WMC_INQUIRY_ADMIN_PREFIX, lang);
      emailService.sendMail(mailConfig.getSupportMail(), mailConfig.getMailFromAddress(),
          mailConfig.getMailFromName(),
          subject, body);

    } catch (IOException | TemplateException | MessagingException ex) {
      log.error(SENT_MAIL_FAILED_LOG_TEMPLATE, ex.getLocalizedMessage());
    }

  }

  public void sendMailToContactedUser(String sender, InquiryType inquiryType,
      InquiryBean inquiryBean, String lang) {
    Map<String, String> model = new HashMap<>();
    model.put(ApplicationConstants.InquiryMailTemplateFields.NAME, inquiryBean.getName());
    model.put(ApplicationConstants.InquiryMailTemplateFields.CONTENT, inquiryBean.getContent());
    model.put(ApplicationConstants.InquiryMailTemplateFields.INQUIRY_TYPE, inquiryType.getValue());
    model
        .put(ApplicationConstants.MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(ApplicationConstants.MailTemplateFields.ROOT_URL, mailConfig.getWmcRootUrl());
    model.put(ApplicationConstants.MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());

    try {
      String subject = String.format(mailDataProvider
              .getMailSubjectCommon(ApplicationConstants.MailType.WMC_POST_INQUIRY, lang),
          inquiryType.getValue());
      String body = mailDataProvider.getMailBody(model, MailTemplateName.WMC_INQUIRY_PREFIX, lang);
      if (!ObjectUtils.isEmpty(sender)) {
        emailService.sendMail(sender, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
            subject, body);
      }
    } catch (IOException | TemplateException | MessagingException ex) {
      log.error(SENT_MAIL_FAILED_LOG_TEMPLATE, ex.getLocalizedMessage());
    }
  }
}
