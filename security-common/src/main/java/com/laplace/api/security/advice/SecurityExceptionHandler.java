package com.laplace.api.security.advice;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.logging.ErrorMessageInfo;
import com.laplace.api.common.logging.LogMessageConfig;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.exceptions.InvalidJwtToken;
import com.laplace.api.security.helper.AuthenticationFailResponseBuilder;
import java.util.Collections;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import twitter4j.TwitterException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

  private final LogMessageConfig logMessageConfig;
  private final AuthenticationFailResponseBuilder failResponseBuilder;

  @Autowired
  public SecurityExceptionHandler(LogMessageConfig logMessageConfig,
      AuthenticationFailResponseBuilder failResponseBuilder) {
    this.logMessageConfig = logMessageConfig;
    this.failResponseBuilder = failResponseBuilder;
  }

  private String getMessage(String errorCode) {
    try {
      ErrorMessageInfo errorMessageInfo = logMessageConfig.getErrorMessageInfo(errorCode);
      return errorMessageInfo.getMessageTemplate();
    } catch (NoSuchElementException e) {
      return errorCode;
    }
  }

  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public BaseResponse handle(AccessDeniedException e) {
    return BaseResponse.builder().responseType(ResponseType.ERROR)
        .message(Collections.singleton(e.getMessage())).code(ErrorCode.ACCESS_DENIED).build();
  }

  @ExceptionHandler({AuthenticationException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseResponse handle(AuthenticationException e) {
    log.error(e.getLocalizedMessage(), e);
    return failResponseBuilder.buildErrorResponse(e);
  }

  @ExceptionHandler({InvalidJwtToken.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public BaseResponse handle(InvalidJwtToken e) {
    log.error(e.getLocalizedMessage(), e);
    return BaseResponse.builder().responseType(ResponseType.ERROR)
        .message(Collections.singleton(e.getMessage())).code(ErrorCode.INVALID_JWT_TOKEN).build();
  }


  @ExceptionHandler({TwitterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse handle(TwitterException e) {
    log.error("TwitterException.ErrorCode: {}, Message: {}", e.getErrorCode(),
        e.getErrorMessage(), e);
    return BaseResponse.builder().responseType(ResponseType.ERROR)
        .message(Collections.singleton(getMessage(ErrorCode.UNAUTHORIZED_OPERATION)))
        .code(ErrorCode.UNAUTHORIZED_OPERATION).build();
  }
}
