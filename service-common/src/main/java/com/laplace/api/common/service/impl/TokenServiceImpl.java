package com.laplace.api.common.service.impl;

import com.laplace.api.common.constants.enums.AppType;
import com.laplace.api.common.model.redis.Token;
import com.laplace.api.common.repository.redis.TokenRepository;
import com.laplace.api.common.service.TokenService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  private final TokenRepository tokenRepository;

  @Autowired
  public TokenServiceImpl(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  public void save(Token token) {
    tokenRepository.save(token);
  }

  @Override
  public Optional<Token> findByToken(String token) {
    return tokenRepository.findById(token);
  }

  @Override
  public List<Token> findByUserId(Integer userId) {
    return tokenRepository.findByUserId(userId);
  }

  @Override
  public void deleteAll(List<Token> tokenList) {
    tokenRepository.deleteAll(tokenList);
  }

  @Override
  public void delete(Token token) {
    tokenRepository.delete(token);
  }

  @Override
  public List<Token> findByUserIdAndAppType(Integer userId, AppType appType) {
    return tokenRepository.findByUserIdAndAppType(userId, appType);
  }
}
