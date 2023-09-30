package com.laplace.api.common.util;

import javax.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

  private static ApplicationContext context;

  public static ApplicationContext getContext() {
    return context;
  }

  @Override
  public void setApplicationContext(@Nonnull ApplicationContext applicationContext)
      throws BeansException {
    context = applicationContext;
  }
}
