package com.laplace.api.cms.service.impl;

import static com.laplace.api.common.util.DateUtil.DATETIME_FORMATTER;
import static com.laplace.api.common.util.DateUtil.DATE_FORMATTER;

import com.laplace.api.cms.service.CMSEmailService;
import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.configuration.email.MailDataProvider;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.MailTemplateFields;
import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.MailTemplateName;
import com.laplace.api.common.model.db.*;
import com.laplace.api.common.repository.db.AddressRepository;
import com.laplace.api.common.service.EmailService;
import com.laplace.api.common.util.DateUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CMSEmailServiceImpl implements CMSEmailService {

  private final EmailService emailService;
  private final MailConfiguration mailConfig;
  private final MailDataProvider mailDataProvider;
  private final AddressRepository addressRepository;


  @Autowired
  public CMSEmailServiceImpl(EmailService emailService, MailConfiguration mailConfig,
      MailDataProvider mailDataProvider,
      AddressRepository addressRepository) {
    this.emailService = emailService;
    this.mailConfig = mailConfig;
    this.mailDataProvider = mailDataProvider;
    this.addressRepository = addressRepository;
  }

  @Override
  public void sendResetPasswordMail(AdminUser user, VerificationToken verificationToken,
      String language) {

    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.CMS_RESET_PASSWORD, language);
    String link = mailConfig.getResetPasswordUrl()
        + ApplicationConstants.EMAIL_URL_TOKEN_PARAMETER + verificationToken.getToken()
        + ApplicationConstants.EMAIL_URL_EMAIL_PARAMETER + user.getEmail();

    String templateNamePrefix = MailTemplateName.CMS_FORGOT_PASSWORD_PREFIX;

    sendMail(link, subject, templateNamePrefix, language, user.getEmail());
  }

  @Override
  public void sendInvitationMail(AdminUser user, VerificationToken verificationToken, Role role,
      String language) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.CMS_INVITE_USER, language);

    String link = mailConfig.getInviteUserUrl()
        + ApplicationConstants.EMAIL_URL_TOKEN_PARAMETER + verificationToken.getToken()
        + ApplicationConstants.EMAIL_URL_ROLE_PARAMETER + role.getRoleId()
        + ApplicationConstants.EMAIL_URL_EMAIL_PARAMETER + user.getEmail();

    String templateNamePrefix = MailTemplateName.INVITE_USER_PREFIX;

    sendMail(link, subject, templateNamePrefix, language, user.getEmail());
  }

  @Override
  public void sendFakeJudgementMail(Item item, AppUser user) {
    String language = Languages.JAPANESE;
    String subject = String.format(mailDataProvider
            .getMailSubjectCommon(ApplicationConstants.MailType.CMS_FAKE_ITEM, language),
        item.getItemId());
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.REQUEST_TIME,
        DateUtil.toJapaneseDateAnnotation(item.getCreatedOn().toLocalDateTime()));
    model.put(MailTemplateFields.REQUEST_ID, item.getPackageId());
    model.put(MailTemplateFields.ITEM_ID, item.getItemId());
    model.put(MailTemplateFields.BRAND_NAME, item.getBrandName());
    model.put(MailTemplateFields.ITEM_NAME, item.getItemName());
    model.put(MailTemplateFields.LINK, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.USER_NAME, user.getFullName());
    model.put(MailTemplateFields.ITEM_URL, mailConfig.getWmcRootUrl()+"/item/"+item.getItemId());

    String templateNamePrefix = MailTemplateName.CMS_FAKE_ITEM_PREFIX;

    sendMail(model, subject, templateNamePrefix, language, user.getEmail());
  }

  @Override
  public void sendOnSaleMail(Item item, AppUser user) {
    String language = Languages.JAPANESE;
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_ON_SALE, language);
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, user.getFullName());
    model.put(MailTemplateFields.ITEM_ID, item.getItemId());
    model.put(MailTemplateFields.ITEM_URL, mailConfig.getWmcRootUrl()+"/item/"+item.getItemId());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());

    String templateNamePrefix = MailTemplateName.ITEM_ON_SALE_PREFIX;

    sendMail(model, subject, templateNamePrefix, language, user.getEmail());
  }

  @Override
  public void sendReadyToDeliveredMail(Order order, AppUser user) {
    String language = Languages.JAPANESE;
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_READY_TO_DELIVERED, language);
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, user.getFullName());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.ITEM_URL,
        mailConfig.getWmcItemUrl() + order.getItem().getItemId());
    model.put(MailTemplateFields.ORDER_ID, order.getOrderId());
    model.put(MailTemplateFields.ITEM_ID, order.getItemId());
    model.put(MailTemplateFields.BRAND_NAME, order.getItem().getBrandName());
    model.put(MailTemplateFields.ITEM_NAME, order.getItem().getItemName());
    model.put(MailTemplateFields.ORDER_DATETIME, DATETIME_FORMATTER.format(order.getCreatedAt()));
    model.put(MailTemplateFields.WRAPPING, order.getGiftWrappingOptions().toString());
    model.put(MailTemplateFields.ESTIMATED_DATETIME_OF_ARRIVAL, DATE_FORMATTER.format(
        order.getCreatedAt().plusDays(ApplicationConstants.ESTIMATED_DELIVERY_DAYS)));
    model.put(MailTemplateFields.RECIPIENT_NAME, order.getShippingName());
    model.put(MailTemplateFields.RECIPIENT_ADDRESS, getFullAddress(order.getShippingAddressId()));
    model.put(MailTemplateFields.DELIVERY_TRACKING_NUMBER, order.getItem().getDeliverySlipNumber());
    model.put(MailTemplateFields.DELIVERY_STATUS_CONFIRMATION_PAGE_URL,
        mailConfig.getWmcDeliveryUrl());

    String templateNamePrefix = MailTemplateName.ITEM_READY_TO_DELIVERED_PREFIX;

    sendMail(model, subject, templateNamePrefix, language, user.getEmail());
  }

  @Override
  public void sendItemReturnCompletedMail(Order order, AppUser user) {
    String language = Languages.JAPANESE;
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_RETURN_COMPLETED, language);
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, user.getFullName());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.ITEM_URL,
        mailConfig.getWmcItemUrl() + order.getItem().getItemId());
    model.put(MailTemplateFields.ITEM_ID, order.getItemId());
    model.put(MailTemplateFields.BRAND_NAME, order.getItem().getBrandName());
    model.put(MailTemplateFields.ITEM_NAME, order.getItem().getItemName());
    model.put(MailTemplateFields.RECIPIENT_NAME, order.getShippingName());
    model.put(MailTemplateFields.RECIPIENT_ADDRESS, getFullAddress(order.getShippingAddressId()));
    model.put(MailTemplateFields.DELIVERY_TRACKING_NUMBER, order.getItem().getDeliverySlipNumber());
    model.put(MailTemplateFields.DELIVERY_STATUS_CONFIRMATION_PAGE_URL,
        mailConfig.getWmcDeliveryUrl());

    String templateNamePrefix = MailTemplateName.ITEM_RETURN_COMPLETED_PREFIX;

    sendMail(model, subject, templateNamePrefix, language, user.getEmail());
  }

  private void sendMail(Map<String, String> model, String subject, String templateNamePrefix,
      String language, String to) {
    try {
      String body = mailDataProvider.getMailBody(model, templateNamePrefix, language);
      emailService.sendMail(to, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
          subject, body);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error("Can't send mail.{}", e.getLocalizedMessage(), e);
    }
  }

  private void sendMail(String link, String subject, String templateNamePrefix, String language,
      String to) {
    Map<String, String> model = new HashMap<>();
    model.put(ApplicationConstants.MailTemplateFields.LINK, link);
    sendMail(model, subject, templateNamePrefix, language, to);
  }

  private String getFullAddress(Integer addressId) {
    Optional<Address> address = addressRepository.findById(addressId);
    return address.map(value -> value.getZip() + " " + value.getPrefectures() + " "
        + value.getMunicipalityChome() + " " + value.getBuildingNameRoomNumber()).orElse("");
  }
}
