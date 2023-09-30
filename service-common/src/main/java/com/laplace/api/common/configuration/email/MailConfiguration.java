package com.laplace.api.common.configuration.email;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:mail-config-${spring.profiles.active}.properties", encoding = "UTF-8")
@Data
public class MailConfiguration {

  @Value("${cms.root.url}")
  private String cmsRootUrl;
  @Value("${mail.from.address}")
  private String mailFromAddress;
  @Value("${mail.from.name}")
  private String mailFromName;
  @Value("${mail.cms.url.login}")
  private String login;
  @Value("${mail.cms.url.invite}")
  private String inviteUserUrl;
  @Value("${mail.cms.url.password.reset}")
  private String resetPasswordUrl;
  @Value("${mail.cms.url.activation}")
  private String userActivation;
  @Value("${invitation.mail.expired.in.hour}")
  private Integer invitationMailExpiration;
  @Value("${otp.expired.in.hour}")
  private Integer otpExpiration;
  @Value("${forgot.password.mail.expired.in.hour}")
  private Integer resetPasswordMailExpiration;
  @Value("${email.expired.in.hour}")
  private Integer defaultMailExpiration;
  @Value("${email.max.number.attempts}")
  private Integer emailRetryAttemp;
  @Value("${mail.wmc.url.activation}")
  private String wmcUserActivation;
  @Value("${mail.wmc.url.password.reset}")
  private String wmcResetPasswordUrl;
  @Value("${wmc.root.url}")
  private String wmcRootUrl;
  @Value("${mail.wmc.url.contact}")
  private String wmcContactUrl;
  @Value("${mail.wmc.url.item}")
  private String wmcItemUrl;
  @Value("${mail.wmc.url.delivery}")
  private String wmcDeliveryUrl;
  @Value("${mail.from.service.name}")
  private String mailServiceName;
  @Value("${mail.to.support}")
  private String supportMail;
  @Value("${mail.to.order}")
  private String orderMail;
  @Value("${mail.wmc.url.my.page}")
  private String wmcMyPageUrl;
}