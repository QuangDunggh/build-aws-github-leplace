package com.laplace.api.common.interceptor;

import com.laplace.api.common.constants.ApplicationConstants;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class RequestHandlingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    MDC.put(ApplicationConstants.REQUEST_ID, UUID.randomUUID().toString());
    MDC.put(ApplicationConstants.LANGUAGE, request.getHeader(ApplicationConstants.ACCEPT_LANGUAGE));
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) {
    log.trace(request.getPathInfo());
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception exception) {
    log.trace(request.getPathInfo());
  }
}
