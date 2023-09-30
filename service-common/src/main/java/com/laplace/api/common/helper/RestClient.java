package com.laplace.api.common.helper;

import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RestClient {

  private String base;
  private RestTemplate rest;
  private HttpHeaders headers;
  private HttpStatus status;
  private String response;

  public RestClient(String baseUrl) {
    Objects.requireNonNull(baseUrl, "base url can't be null");
    this.base = baseUrl;
    ErrorHandler handler = new ErrorHandler();
    this.rest = new RestTemplate();
    this.rest.setErrorHandler(handler);
    this.headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Accept", "*/*");
  }

  public String get(String uri) {
    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
    log.info("Request uri=" + base + uri);
    ResponseEntity<String> responseEntity = rest
        .exchange(base + uri, HttpMethod.GET, requestEntity, String.class);
    this.setStatus(responseEntity.getStatusCode());
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      response = responseEntity.getBody();
    }
    return response;
  }

  public String post(String uri, String json) throws HttpClientErrorException {
    HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
    ResponseEntity<String> responseEntity = rest
        .exchange(base + uri, HttpMethod.POST, requestEntity, String.class);
    this.setStatus(responseEntity.getStatusCode());
    return responseEntity.getBody();
  }

  public HttpStatus getStatus() {
    return status;
  }

  private void setStatus(HttpStatus status) {
    this.status = status;
  }

  class ErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
      response = IOUtils.toString(clientHttpResponse.getBody(), "UTF8");
      status = clientHttpResponse.getStatusCode();
    }
  }
}
