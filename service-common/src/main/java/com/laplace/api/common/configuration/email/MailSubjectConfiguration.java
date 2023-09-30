package com.laplace.api.common.configuration.email;

import com.laplace.api.common.constants.Languages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:mail-subject.properties", encoding = "UTF-8")

public class MailSubjectConfiguration {

  //TODO have to find out mail subject
  private final Environment environment;

  @Autowired
  public MailSubjectConfiguration(Environment environment) {
    this.environment = environment;
  }


  public String getRegistrationEmailJapaneseSubject() {
    return environment.getProperty("registration.email.subject.ja");
  }

  public String getRegistrationEmailEnglishSubject() {
    return environment.getProperty("registration.email.subject.en");
  }

  public String getFinishRegistrationEmailJapaneseSubject() {
    return environment.getProperty("finish.registration.email.subject.ja");
  }

  public String getFinishRegistrationEmailEnglishSubject() {
    return environment.getProperty("finish.registration.email.subject.en");
  }

  public String getForgotPasswordEmailJapaneseSubject() {
    return environment.getProperty("forgotpassword.email.subject.ja");
  }

  public String getForgotPasswordEmailEnglishSubject() {
    return environment.getProperty("forgotpassword.email.subject.en");
  }

  public String getMailSubject(String key, String language) {
    return environment.getProperty(String.join(".", key, Languages.JAPANESE));
  }
}
