package com.laplace.api.common.configuration.notification;

import com.laplace.api.common.constants.Languages;
import com.laplace.api.common.constants.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
@PropertySource(value = "classpath:notification-template.properties", encoding = "UTF-8")
@Slf4j
public class NotificationTemplateConfiguration {

  private final Environment environment;

  @Autowired
  public NotificationTemplateConfiguration(Environment environment) {
    this.environment = environment;
  }

  public String getTitle(NotificationType notificationType) {
    return getTemplateJa(notificationType.getTitleTemplate());
  }

  public String getMessage(NotificationType notificationType) {
    return getTemplateJa(notificationType.getMessageTemplate());
  }


  public String getTemplateEn(String key) {
    return getTemplate(key, Languages.ENGLISH);
  }

  public String getTemplateJa(String key) {
    return getTemplate(key, Languages.JAPANESE);
  }

  public String getDeepLink(String key) {
    return environment.getProperty(key, StringUtils.EMPTY);
  }

  private String getTemplate(String key, String language) {
    return environment.getProperty(String.join(".", key, language), StringUtils.EMPTY);
  }

  private String format(String template, Map<String, String> replacements) {
    for (String key : replacements.keySet()) {
      template = template.replace(key, replacements.get(key));
    }
    return template;
  }
}
