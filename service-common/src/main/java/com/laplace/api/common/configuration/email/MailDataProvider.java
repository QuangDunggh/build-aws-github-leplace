package com.laplace.api.common.configuration.email;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.Languages;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Slf4j
@Component
public class MailDataProvider {

  private final HttpServletRequest request;
  private final MailSubjectConfiguration mailSubjectConfig;

  @Autowired
  public MailDataProvider(HttpServletRequest request, MailSubjectConfiguration mailSubjectConfig) {
    this.request = request;
    this.mailSubjectConfig = mailSubjectConfig;
  }

  @Bean(name = "freemarkerConfig")
  public Configuration freemarkerConfig() {
    Configuration config = new Configuration(Configuration.VERSION_2_3_28);
    config.setClassForTemplateLoading(this.getClass(), "/templates/");
    return config;
  }

  public String getMailBody(Map model, String prefix, String lang)
      throws IOException, TemplateException {

    String templateName = getTemplateName(prefix, lang);
    Template template = freemarkerConfig().getTemplate(templateName);
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
  }

  private String getTemplateName(String prefix, String lang) {
    String result = String.join("-", prefix, Languages.JAPANESE);
    return result + ".ftl";
  }

  public String getHost() {
    StringBuffer url = request.getRequestURL();
    String uri = request.getRequestURI();
    return url.substring(0, url.indexOf(uri));
  }

  public String getMailSubject(Integer mailType, String lang) {
    if (Languages.JAPANESE.equals(lang)) {
      return getJapaneseSubjects(mailType);
    } else {
      return getEnglishSubjects(mailType);
    }
  }

  private String getJapaneseSubjects(Integer mailType) {
    //TODO: Find out Japanese mail subject and implement logic for appropriate subject generation
    return getEnglishSubjects(mailType);
  }

  public String getMailSubjectCommon(Integer mailType, String lang) {
    return mailSubjectConfig.getMailSubject(ApplicationConstants.mailSubject.get(mailType), lang);
  }

  private String getEnglishSubjects(Integer mailType) {
    String subject;
    switch (mailType) {
      case ApplicationConstants.MailType.CMS_RESET_PASSWORD:
      case ApplicationConstants.MailType.WMC_RESET_PASSWORD:
        subject = mailSubjectConfig.getForgotPasswordEmailEnglishSubject();
        break;
      case ApplicationConstants.MailType.CMS_INVITE_USER:
      case ApplicationConstants.MailType.WMC_ACTIVE_USER:
        subject = mailSubjectConfig.getRegistrationEmailEnglishSubject();
        break;
      default:
        subject = "";
    }
    return subject;
  }
}
