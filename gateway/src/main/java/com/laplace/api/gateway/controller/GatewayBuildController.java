package com.laplace.api.gateway.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/p/gateway/build")
public class GatewayBuildController {

  private BuildProperties buildProperties;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

  @Autowired
  public GatewayBuildController(BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  private static String getServerBaseUrl(HttpServletRequest request) {
    String schema = request.getHeader("x-forwarded-proto");
    if (schema == null || "".equals(schema)) {
      schema = request.getScheme();
    }
    schema += "://";
    String host = request.getHeader("x-forwarded-host");
    if (host == null || "".equals(host)) {
      host = request.getServerName() + ":" + request.getServerPort();
    }
    return schema + host;
  }

  @GetMapping()
  public Map<String, Object> retrieveVersion(@RequestParam(value = "offset_hours")
      String offsetHours,
      HttpServletRequest request) {
    Map<String, Object> buildInfo = new HashMap<>();
    StringBuffer url = request.getRequestURL();
    String uri = request.getRequestURI();

    //If there is no plus sign then prepend a plus sign
    if (!offsetHours.contains("-")) {
      offsetHours = "+" + offsetHours;
    }
    String host = url.substring(0, url.indexOf(uri));
    log.debug("host: " + host);

    String realHost = request.getHeader(HttpHeaders.HOST);
    // client's IP address
    String remoteAddr = request.getRemoteAddr();
    String domain = request.getServerName();
    String serverBaseUrl = getServerBaseUrl(request);

    buildInfo.put("host", host);
    buildInfo.put("realHost", realHost);
    buildInfo.put("serverBaseUrl", serverBaseUrl);
    buildInfo.put("remoteAddress", remoteAddr);
    buildInfo.put("domain", domain);
    buildInfo.put("name", buildProperties.getName());
    buildInfo.put("group", buildProperties.getGroup());
    buildInfo.put("version", buildProperties.getVersion());
    buildInfo.put("artifact", buildProperties.getArtifact());
    buildInfo.put("time", dateTimeFormatter.format(
        ZonedDateTime.ofInstant(buildProperties.getTime(), ZoneId.of(offsetHours))));
    return buildInfo;
  }
}
