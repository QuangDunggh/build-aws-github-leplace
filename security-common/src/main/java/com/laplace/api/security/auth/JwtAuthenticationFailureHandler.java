package com.laplace.api.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.security.helper.AuthenticationFailResponseBuilder;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper mapper;
  private final AuthenticationFailResponseBuilder failResponseBuilder;

  @Autowired
  public JwtAuthenticationFailureHandler(ObjectMapper mapper,
      AuthenticationFailResponseBuilder failResponseBuilder) {
    this.mapper = mapper;
    this.failResponseBuilder = failResponseBuilder;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException e) throws IOException, ServletException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    BaseResponse errorResponse = failResponseBuilder.buildErrorResponse(e);
    mapper.writeValue(response.getWriter(), errorResponse);
  }
}
