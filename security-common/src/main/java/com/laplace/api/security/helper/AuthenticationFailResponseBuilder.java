package com.laplace.api.security.helper;

import com.laplace.api.common.constants.ErrorCode;
import com.laplace.api.common.constants.enums.ResponseType;
import com.laplace.api.common.logging.LogMessageConfig;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.exceptions.JwtExpiredTokenException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationFailResponseBuilder {

  private final LogMessageConfig logMessageConfig;

  @Autowired
  public AuthenticationFailResponseBuilder(LogMessageConfig logMessageConfig) {
    this.logMessageConfig = logMessageConfig;
  }

  /**
   * Build error response for authentication exception
   *
   * @param e authentication exception
   * @return error response based on authentication exception type
   */
  public BaseResponse buildErrorResponse(AuthenticationException e) {
    BaseResponse errorResponse;
    if (e instanceof UsernameNotFoundException) {
      errorResponse = BaseResponse.builder().responseType(ResponseType.ERROR)
          .message(Collections.singleton(
              logMessageConfig.getErrorMessageInfo(ErrorCode.INVALID_USER).getMessageTemplate()))
          .code(ErrorCode.INVALID_USER).build();
      return errorResponse;
    } else if (e instanceof BadCredentialsException) {
      errorResponse = BaseResponse.builder().responseType(ResponseType.ERROR)
          .message(Collections.singleton(
              logMessageConfig.getErrorMessageInfo(ErrorCode.AUTHENTICATION).getMessageTemplate()))
          .code(ErrorCode.AUTHENTICATION).build();
    } else if (e instanceof JwtExpiredTokenException) {
      errorResponse = BaseResponse.builder().responseType(ResponseType.ERROR)
          .message(Collections.singleton(
              logMessageConfig.getErrorMessageInfo(ErrorCode.JWT_TOKEN_EXPIRED)
                  .getMessageTemplate()))
          .code(ErrorCode.JWT_TOKEN_EXPIRED).build();
    } else {
      errorResponse = BaseResponse.builder().responseType(ResponseType.ERROR)
          .message(Collections.singleton(e.getMessage()))
          .code(ErrorCode.INVALID_JWT_TOKEN).build();
    }
    return errorResponse;
  }
}
