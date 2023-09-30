package com.laplace.api.security.config;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.repository.redis.TokenRepository;
import com.laplace.api.security.auth.JwtAuthenticationProvider;
import com.laplace.api.security.auth.JwtTokenAuthenticationProcessingFilter;
import com.laplace.api.security.auth.SkipPathRequestMatcher;
import com.laplace.api.security.auth.jwt.extractor.TokenExtractor;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class LaplaceSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AuthenticationFailureHandler failureHandler;

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Autowired
  private TokenExtractor tokenExtractor;

  @Autowired
  private TokenRepository tokenRepository;

  @Value("${laplace.origin}")
  private String[] siteOrigin;

  private JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(
      List<String> pathsToSkip, String pattern) throws Exception {
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
    JwtTokenAuthenticationProcessingFilter filter
        = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor,
        matcher, new JwtSettings(), tokenRepository);
    filter.setAuthenticationManager(authenticationManagerBean());
    return filter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(jwtAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // public api list
    List<String> permitAllEndpointList = Arrays.asList(
        SecurityConfigConstants.PUBLIC_URL_PREFIX,
        SecurityConfigConstants.CLIENT_REFRESH_TOKEN_URL,
        SecurityConfigConstants.CMS_REFRESH_TOKEN_URL
    );

    http
        .cors().configurationSource(new LaplaceCorsConfigurationSource())
        .and()
        .headers()
        .frameOptions().sameOrigin()
        .and()
        .csrf().disable()
        .exceptionHandling()

        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
        .antMatchers(permitAllEndpointList.toArray(new String[0]))
        .permitAll()
        .and()
        .authorizeRequests()
        .antMatchers(SecurityConfigConstants.AUTHENTICATED_URL_PREFIX)
        .authenticated() // Protected API End-points
        .and()
        .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList,
            SecurityConfigConstants.AUTHENTICATED_URL_PREFIX),
            UsernamePasswordAuthenticationFilter.class);
  }

  public class LaplaceCorsConfigurationSource implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      corsConfiguration.setAllowCredentials(true);
      corsConfiguration.setAllowedOrigins(Arrays.asList(siteOrigin));

      corsConfiguration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
      corsConfiguration.setAllowedHeaders(
          Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
              "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
              "Cache-Control", "Content-Type",
              "Authorization", "Accept-Language", "Accept",
              ApplicationConstants.LAPLACE_API_ACCESS_KEY));

      corsConfiguration.setMaxAge(3600L);
      return corsConfiguration;
    }
  }
}
