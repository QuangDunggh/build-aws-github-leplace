package com.laplace.api.web.service;

import static com.laplace.api.common.constants.ApplicationConstants.StringUtils.SPACE;
import static com.laplace.api.common.util.DateUtil.DATETIME_FORMATTER;
import static com.laplace.api.common.util.DateUtil.DATE_FORMATTER;

import com.laplace.api.common.configuration.email.MailConfiguration;
import com.laplace.api.common.configuration.email.MailDataProvider;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.MailTemplateFields;
import com.laplace.api.common.constants.ApplicationConstants.MyPage;
import com.laplace.api.common.constants.MailTemplateName;
import com.laplace.api.common.constants.enums.DeliveryFeeBearer;
import com.laplace.api.common.constants.enums.WithdrawalReason;
import com.laplace.api.common.dto.request.AccountWithdrawRequestDto;
import com.laplace.api.common.model.db.Address;
import com.laplace.api.common.model.db.AppUser;
import com.laplace.api.common.model.db.AppUserProfile;
import com.laplace.api.common.model.db.Item;
import com.laplace.api.common.model.db.Order;
import com.laplace.api.common.model.db.Payment;
import com.laplace.api.common.model.db.VerificationToken;
import com.laplace.api.common.repository.db.AddressRepository;
import com.laplace.api.common.repository.db.AppUserProfileRepository;
import com.laplace.api.common.service.EmailService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
public class WMCEmailService {

  private static final String SENT_MAIL_FAILED_LOG_TEMPLATE = "Can't send mail.{}";
  private final EmailService emailService;
  private final MailConfiguration mailConfig;
  private final MailDataProvider mailDataProvider;
  private final AppUserProfileRepository appUserProfileRepository;
  private final AddressRepository addressRepository;

  @Autowired
  public WMCEmailService(EmailService emailService, MailConfiguration mailConfig,
      MailDataProvider mailDataProvider,
      AppUserProfileRepository appUserProfileRepository,
      AddressRepository addressRepository) {
    this.emailService = emailService;
    this.mailConfig = mailConfig;
    this.mailDataProvider = mailDataProvider;
    this.appUserProfileRepository = appUserProfileRepository;
    this.addressRepository = addressRepository;
  }

  /**
   * send user activation mail
   *
   * @param recipient App user
   * @param token     verification token
   * @param language  language
   */
  void sendActivationEmail(String recipient, VerificationToken token, String language) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_ACTIVE_USER, language);

    String link = mailConfig.getWmcUserActivation()
        + ApplicationConstants.EMAIL_URL_TOKEN_PARAMETER + token.getToken()
        + ApplicationConstants.EMAIL_URL_EMAIL_PARAMETER + token.getContext();
    String templateNamePrefix = MailTemplateName.WMC_ACTIVE_USER_PREFIX;

    try {
      sendMail(link, subject, token.getToken(), templateNamePrefix, language, recipient);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(SENT_MAIL_FAILED_LOG_TEMPLATE, e.getLocalizedMessage(), e);
    }
  }

  private void sendMail(String link, String subject, String code, String templateNamePrefix,
      String language,
      String to) throws IOException, TemplateException, MessagingException {
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.LINK, link);
    model.put(MailTemplateFields.CODE, code);
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.ROOT_URL, mailConfig.getWmcRootUrl());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, language);
    emailService.sendMail(to, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
  }

  void sendResetPasswordMail(AppUser user, VerificationToken verificationToken, String lang) {

    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_RESET_PASSWORD, lang);
    String link = mailConfig.getWmcResetPasswordUrl()
        + ApplicationConstants.EMAIL_URL_TOKEN_PARAMETER + verificationToken.getToken()
        + ApplicationConstants.EMAIL_URL_EMAIL_PARAMETER + user.getEmail();

    String templateNamePrefix = MailTemplateName.FORGOT_PASSWORD_PREFIX;

    try {
      sendMail(link, subject, templateNamePrefix, lang, user, null);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(SENT_MAIL_FAILED_LOG_TEMPLATE, e.getLocalizedMessage(), e);
    }
  }

  void sendResetEmailAddressMail(String email, AppUser user, VerificationToken verificationToken,
      String lang) {

    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_RESET_EMAIL, lang);
    String link = mailConfig.getWmcUserActivation()
        + ApplicationConstants.EMAIL_URL_TOKEN_PARAMETER + verificationToken.getToken()
        + ApplicationConstants.EMAIL_URL_EMAIL_PARAMETER + email;

    String templateNamePrefix = MailTemplateName.WMC_RESET_USER_EMAIL_PREFIX;
    user.setEmail(email);
    try {
      sendMail(link, subject, templateNamePrefix, lang, user, null);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(SENT_MAIL_FAILED_LOG_TEMPLATE, e.getLocalizedMessage(), e);
    }
  }

  void sendPasswordResetSuccessMail(AppUser user, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.PASSWORD_RESET_SUCCESS, lang);
    String templateNamePrefix = MailTemplateName.PASSWORD_RESET_SUCCESS_PREFIX;
    try {
      sendMail(null, subject, templateNamePrefix, lang, user, null);
    } catch (IOException | TemplateException | MessagingException e) {
      log.error(SENT_MAIL_FAILED_LOG_TEMPLATE, e.getLocalizedMessage(), e);
    }
  }

  @Async("taskExecutor")
  void sendOrderConfirmMail(String email, Integer userId, String emailDomain, String lang)
      throws IOException, TemplateException, MessagingException {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_CONFIRM_ORDER, lang);
    String templateNamePrefix = MailTemplateName.WMC_ORDER_CONFIRM_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.USER_NAME, getFullName(userId));
    model.put(MailTemplateFields.EMAIL_DOMAIN, emailDomain);
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.ROOT_URL, mailConfig.getWmcRootUrl());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);

  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendAccountWithdrawalMail(String email, String name, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_ACCOUNT_WITHDRAW, lang);
    String templateNamePrefix = MailTemplateName.WMC_ACCOUNT_WITHDRAW_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.EMAIL_DOMAIN, email);
    model.put(MailTemplateFields.USER_NAME, name);
    model.put(MailTemplateFields.ROOT_URL, mailConfig.getWmcRootUrl());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendAccountWithdrawalMailToAdmin(String email, String surnameOrderedName,
      AccountWithdrawRequestDto withdrawRequestDto, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_ACCOUNT_WITHDRAW_ADMIN, lang);
    String templateNamePrefix = MailTemplateName.WMC_ACCOUNT_WITHDRAW_ADMIN_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.EMAIL, email);
    model.put(MailTemplateFields.USER_NAME, surnameOrderedName);
    model.put(MailTemplateFields.WITHDRAWAL_REASONS,
        convertToHtmlList(withdrawRequestDto.getReason()));
    model.put(MailTemplateFields.DETAILS_REASON, withdrawRequestDto.getDetailsReason());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(mailConfig.getSupportMail(), mailConfig.getMailFromAddress(),
        mailConfig.getMailFromName(), subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendItemPurchasedMailToBuyer(String email, Order order, Item item, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_PURCHASED_BUYER, lang);
    String templateNamePrefix = MailTemplateName.ITEM_PURCHASED_BUYER_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, getFullName(order.getUserId()));
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.ORDER_ID, order.getOrderId());
    model.put(MailTemplateFields.ITEM_ID, order.getItemId());
    model.put(MailTemplateFields.ITEM_URL, mailConfig.getWmcItemUrl() + item.getItemId());
    model.put(MailTemplateFields.BRAND_NAME, item.getBrandName());
    model.put(MailTemplateFields.ITEM_NAME, item.getItemName());
    model.put(MailTemplateFields.SELLER_NAME, getUserName(order.getSellerId()));
    model.put(MailTemplateFields.PAYMENT_AMOUNT, order.getOrderAmount().toString());
    model.put(MailTemplateFields.ORDER_DATETIME, DATETIME_FORMATTER.format(order.getCreatedAt()));
    model.put(MailTemplateFields.DELIVERY_FEES_BEARER, item.getDeliveryFeeBearer().toString());
    model.put(MailTemplateFields.ESTIMATED_DATETIME_OF_ARRIVAL, DATE_FORMATTER.format(
        order.getCreatedAt().plusDays(ApplicationConstants.ESTIMATED_DELIVERY_DAYS)));
    model.put(MailTemplateFields.RECIPIENT_NAME, order.getShippingName());
    model.put(MailTemplateFields.RECIPIENT_ADDRESS, getFullAddress(order.getShippingAddressId()));
    model.put(MailTemplateFields.ESTIMATED_DELIVERY_DAYS,
        String.valueOf(ApplicationConstants.ESTIMATED_DELIVERY_DAYS));
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
    emailService.sendMail(mailConfig.getOrderMail(), mailConfig.getMailFromAddress(),
        mailConfig.getMailFromName(), subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendItemPurchasedMailToSeller(String email, Order order, Item item, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_PURCHASED_SELLER, lang);
    String templateNamePrefix = MailTemplateName.ITEM_PURCHASED_SELLER_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, getFullName(order.getSellerId()));
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.ITEM_URL, mailConfig.getWmcItemUrl() + item.getItemId());
    model.put(MailTemplateFields.ITEM_ID, order.getItemId());
    model.put(MailTemplateFields.BRAND_NAME, item.getBrandName());
    model.put(MailTemplateFields.ITEM_NAME, item.getItemName());
    model.put(MailTemplateFields.BUYER_NAME, getUserName(order.getUserId()));
    model.put(MailTemplateFields.ITEM_PRICE, item.getDisplayPrice().toString());
    model.put(MailTemplateFields.ORDER_DATETIME, DATETIME_FORMATTER.format(order.getCreatedAt()));
    model.put(MailTemplateFields.DELIVERY_FEES_BEARER, item.getDeliveryFeeBearer().toString());
    model.put(MailTemplateFields.DEPOSITED_AMOUNT,
        String.valueOf(order.getPayments().stream().mapToInt(Payment::getSellerAmount).sum()));
    model.put(MailTemplateFields.ESTIMATED_DELIVERY_DAYS,
        String.valueOf(ApplicationConstants.ESTIMATED_DELIVERY_DAYS));

    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
    emailService.sendMail(mailConfig.getOrderMail(), mailConfig.getMailFromAddress(),
        mailConfig.getMailFromName(), subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendItemCancelMail(String email, Order order, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_CANCELED, lang);
    String templateNamePrefix = MailTemplateName.ITEM_CANCELED_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, getFullName(order.getUserId()));
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.ITEM_URL, mailConfig.getWmcItemUrl() + order.getItemId());
    model.put(MailTemplateFields.ITEM_ID, order.getItemId());
    model.put(MailTemplateFields.ORDER_DATETIME, DATETIME_FORMATTER.format(order.getCreatedAt()));
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
    emailService.sendMail(mailConfig.getOrderMail(), mailConfig.getMailFromAddress(),
        mailConfig.getMailFromName(), subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendItemDisplayTimeExceededMail(String email, Item item, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_DISPLAY_TIME_EXCEEDED, lang);
    String templateNamePrefix = MailTemplateName.ITEM_DISPLAY_TIME_EXCEEDED_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, getFullName(item.getSellerId()));
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.ITEM_URL, mailConfig.getWmcItemUrl() + item.getItemId());
    model.put(MailTemplateFields.ITEM_ID, item.getItemId());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
    emailService.sendMail(mailConfig.getOrderMail(), mailConfig.getMailFromAddress(),
        mailConfig.getMailFromName(), subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendItemNegotiationMail(String email, Item item, String sellerName,
      String negotiatorName, Integer negotiationPrice, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.ITEM_PRICE_NEGOTIATION, lang);
    String templateNamePrefix = MailTemplateName.ITEM_PRICE_NEGOTIATION_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.USER_NAME, sellerName);
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.NEGOTIATOR_NAME, negotiatorName);
    model.put(MailTemplateFields.ITEM_NAME, item.getItemName());
    model.put(MailTemplateFields.CURRENT_PRICE, item.getDisplayPrice().toString());
    model.put(MailTemplateFields.DISCOUNT_REQUEST_PRICE, negotiationPrice.toString());
    model.put(MailTemplateFields.SELL_CLOSET_URL,
        mailConfig.getWmcMyPageUrl() + MyPage.SELL_CLOSET);

    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendIdVerificationFailedMail(String email, String lang) {
    String subject = mailDataProvider
            .getMailSubjectCommon(ApplicationConstants.MailType.WMC_ID_VERIFICATION_FAILED, lang);
    String templateNamePrefix = MailTemplateName.WMC_ID_VERIFICATION_FAILED_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    model.put(MailTemplateFields.SUPPORT_MAIL, mailConfig.getSupportMail());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
            subject, body);
  }

  @SneakyThrows
  @Async("taskExecutor")
  public void sendIdVerificationSuccessMail(String email, String lang) {
    String subject = mailDataProvider
        .getMailSubjectCommon(ApplicationConstants.MailType.WMC_ID_VERIFICATION_SUCCESS, lang);
    String templateNamePrefix = MailTemplateName.WMC_ID_VERIFICATION_SUCCESS_PREFIX;
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.SELL_REQUEST_URL, mailConfig.getWmcMyPageUrl() +
        MyPage.LISTING_REQUEST);
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, lang);

    emailService.sendMail(email, mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
        subject, body);
  }

  private void sendMail(String link, String subject, String templateNamePrefix, String language,
      AppUser user, String emailDomain) throws IOException, TemplateException, MessagingException {
    Map<String, String> model = new HashMap<>();
    model.put(MailTemplateFields.LINK, link);
    model.put(MailTemplateFields.USER_NAME, user.getFullName());
    model.put(MailTemplateFields.EMAIL_DOMAIN, emailDomain);
    model.put(MailTemplateFields.SERVICE_NAME, mailConfig.getMailServiceName());
    model.put(MailTemplateFields.ROOT_URL, mailConfig.getWmcRootUrl());
    model.put(MailTemplateFields.CONTACT_URL, mailConfig.getWmcContactUrl());
    String body = mailDataProvider.getMailBody(model, templateNamePrefix, language);
    emailService
        .sendMail(user.getEmail(), mailConfig.getMailFromAddress(), mailConfig.getMailFromName(),
            subject, body);
  }

  private String getFullName(Integer userId) {
    Optional<AppUserProfile> appUserProfile = appUserProfileRepository.findById(userId);
    return appUserProfile.map(
        userProfile -> userProfile.getFirstName() + SPACE + userProfile.getLastName()).orElse("");
  }

  private String getUserName(Integer userId) {
    Optional<AppUserProfile> appUserProfile = appUserProfileRepository.findById(userId);
    return appUserProfile.map(AppUserProfile::getUserName).orElse("");
  }

  private String getFullAddress(Integer addressId) {
    Optional<Address> address = addressRepository.findById(addressId);
    return address.map(
            value -> value.getZip() + " " + value.getPrefectures() + " " + value.getMunicipalityChome()
                .replace(":", " ") + " " + value.getStreet() + " " + value.getBuildingNameRoomNumber())
        .orElse("");
  }

  private String convertToHtmlList(List<WithdrawalReason> withdrawalReasons) {
    return withdrawalReasons.stream().map(reason -> "・" + reason.getValue() + "<br/>")
        .collect(Collectors.joining());
  }
}
