package com.laplace.api.common.interceptor;

import com.laplace.api.common.configuration.aws.LaplaceSecretsManager;
import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
@Slf4j
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

  private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
      MediaType.APPLICATION_FORM_URLENCODED,
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.MULTIPART_FORM_DATA
  );
  @Autowired
  private LaplaceSecretsManager laplaceSecretsManager;

  private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
    val queryString = request.getQueryString();
    if (queryString == null) {
      log.info("{} {} {}", prefix, request.getMethod(), request.getRequestURI());
    } else {
      log.info("{} {} {}?{}", prefix, request.getMethod(), request.getRequestURI(), queryString);
    }
    if (log.isTraceEnabled()) {
      Collections.list(request.getHeaderNames()).forEach(headerName ->
          Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
              log.trace("{} {}: {}", prefix, headerName, headerValue)));
    }
    log.info("{}", prefix);
  }

  private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
    val content = request.getContentAsByteArray();
    if (content.length > 0) {
      logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix);
    }
  }

  private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
    val status = response.getStatus();
    log.info("{} {} {}", prefix, status, HttpStatus.valueOf(status).getReasonPhrase());
    if (log.isTraceEnabled()) {
      response.getHeaderNames().forEach(headerName ->
          response.getHeaders(headerName).forEach(headerValue ->
              log.trace("{} {}: {}", prefix, headerName, headerValue)));
    }
    log.info("{}", prefix);
    val content = response.getContentAsByteArray();
    if (content.length > 0) {
      logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix);
    }
  }

  private static void logContent(byte[] content, String contentType, String contentEncoding,
      String prefix) {
    val mediaType = MediaType.valueOf(contentType);
    val visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    if (visible) {
      try {
        val contentString = new String(content, contentEncoding);
        Stream.of(contentString.split("\r\n" +
            "|\r|\n")).forEach(line -> log.info("{} {}", prefix, line));
      } catch (UnsupportedEncodingException e) {
        log.info("{} [{} bytes content]", prefix, content.length);
      }
    } else {
      log.info("{} [{} bytes content]", prefix, content.length);
    }
  }

  private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
    if (request instanceof ContentCachingRequestWrapper) {
      return (ContentCachingRequestWrapper) request;
    } else {
      return new ContentCachingRequestWrapper(request);
    }
  }

  private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
    if (response instanceof ContentCachingResponseWrapper) {
      return (ContentCachingResponseWrapper) response;
    } else {
      return new ContentCachingResponseWrapper(response);
    }
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    if (!request.getRequestURI().contains("p/gateway/build")) {
      String apiAccessKey = request.getHeader(ApplicationConstants.LAPLACE_API_ACCESS_KEY);
      if (apiAccessKey == null || !apiAccessKey
          .equals(laplaceSecretsManager.getSecretsManagerKey().getApiAccessKey())) {
        if (laplaceSecretsManager.getSecretsManagerKey().getApiAccessKey()
            .matches(ApplicationConstants.ONLY_DIGIT)) {
          response.sendError(ErrorCode.MAINTENANCE_MODE, "Maintenance mode");
        } else {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access!");
        }

        return;
      }
    }

    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
    } else {
      doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
    }
  }

  private void doFilterWrapped(ContentCachingRequestWrapper request,
      ContentCachingResponseWrapper response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      beforeRequest(request);
      filterChain.doFilter(request, response);
    } finally {
      afterRequest(request, response);
      response.copyBodyToResponse();
    }
  }

  private void beforeRequest(ContentCachingRequestWrapper request) {
    logRequestHeader(request, request.getRemoteAddr() + "|>");
  }

  private void afterRequest(ContentCachingRequestWrapper request,
      ContentCachingResponseWrapper response) {
    logRequestHeader(request, request.getRemoteAddr() + "|>");
    logRequestBody(request, request.getRemoteAddr() + "|>");
    logResponse(response, request.getRemoteAddr() + "|<");
  }
}