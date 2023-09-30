package com.laplace.api.security.auth;


import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.repository.redis.TokenRepository;
import com.laplace.api.security.auth.jwt.extractor.TokenExtractor;
import com.laplace.api.security.config.JwtSettings;
import com.laplace.api.security.config.SecurityConfigConstants;
import com.laplace.api.security.constants.SecurityConstants;
import com.laplace.api.security.exceptions.AuthMethodNotSupportedException;
import com.laplace.api.security.exceptions.InvalidJwtToken;
import com.laplace.api.security.model.token.RawAccessJwtToken;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  private final AuthenticationFailureHandler failureHandler;
  private final TokenExtractor tokenExtractor;
  private JwtSettings jwtSettings;
  private TokenRepository tokenRepository;

  public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
      TokenExtractor tokenExtractor, RequestMatcher matcher,
      JwtSettings jwtSettings,
      TokenRepository tokenRepository) {
    super(matcher);
    this.failureHandler = failureHandler;
    this.tokenExtractor = tokenExtractor;
    this.jwtSettings = jwtSettings;
    this.tokenRepository = tokenRepository;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response)
      throws AuthenticationException {

    String tokenPayload = request.getHeader(SecurityConfigConstants.AUTHENTICATION_HEADER_NAME);
    if (tokenPayload != null && !tokenPayload.isEmpty()) {
      String tokenValue = tokenExtractor.extract(tokenPayload);
      Optional<Token> tokenOptional = tokenRepository.findById(tokenValue);
      Token token =  tokenOptional.orElseThrow(() -> new InvalidJwtToken("Invalid Payload"));

      validatePathWithActivation(request, token); // Activate and profile complete check

      RawAccessJwtToken rawAccessJwtToken = new RawAccessJwtToken(tokenValue);
      return getAuthenticationManager().authenticate(new JwtAuthenticationToken(rawAccessJwtToken));
    } else {
      throw new AuthMethodNotSupportedException("Unauthorized. No payload found");
    }
  }

  private void validatePathWithActivation(HttpServletRequest request, Token token) {

    if (request.getRequestURI().contains(SecurityConstants.EMAIL_UPDATE_API_ENDPOINT) && !token
        .getIsActive()) {
      return;
    }
    if (!token.getIsActive()) {
      throw new InvalidJwtToken("Please confirm your email first!");
    }
    if (!request.getRequestURI().contains(SecurityConstants.PROFILE_API_ENDPOINT) && !token.getProfileComplete()) {
      throw new InvalidJwtToken("Please complete your profile first!");
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    failureHandler.onAuthenticationFailure(request, response, failed);
  }
}
