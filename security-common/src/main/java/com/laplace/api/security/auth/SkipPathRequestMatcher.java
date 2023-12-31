package com.laplace.api.security.auth;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SkipPathRequestMatcher implements RequestMatcher {

  private OrRequestMatcher matchers;
  private RequestMatcher processingMatcher;

  public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
    Objects.requireNonNull(pathsToSkip);
    List<RequestMatcher> m = pathsToSkip.stream().map(AntPathRequestMatcher::new)
        .collect(Collectors.toList());
    matchers = new OrRequestMatcher(m);
    processingMatcher = new AntPathRequestMatcher(processingPath);
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    if (matchers.matches(request)) {
      return false;
    }
    return processingMatcher.matches(request);
  }
}
